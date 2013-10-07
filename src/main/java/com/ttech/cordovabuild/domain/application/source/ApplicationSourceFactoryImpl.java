/*
 * Copyright © 2013 Turkcell Teknoloji Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ttech.cordovabuild.domain.application.source;

import com.ttech.cordovabuild.domain.application.ApplicationConfig;
import com.ttech.cordovabuild.domain.application.ApplicationConfigurationException;
import com.ttech.cordovabuild.domain.asset.AssetRef;
import com.ttech.cordovabuild.domain.asset.AssetService;
import com.ttech.cordovabuild.domain.asset.InputStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.ttech.cordovabuild.infrastructure.archive.ArchiveUtils.compressDirectory;
import static com.ttech.cordovabuild.infrastructure.archive.ArchiveUtils.extractFiles;

@Service
public class ApplicationSourceFactoryImpl implements ApplicationSourceFactory {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ApplicationSourceFactoryImpl.class);

    @Autowired
    AssetService assetService;

    public class ApplicationSourceImpl implements ApplicationSource {

        private Path localPath;
        private ApplicationConfig config;

        public ApplicationSourceImpl(Path localPath) {
            this.localPath = localPath;
        }

        @Override
        public Path getLocalPath() {
            return localPath;
        }

        @Override
        public ApplicationConfig getApplicationConfig() {
            if (config == null)
                config = getApplicationConfigInner(localPath);
            return config;
        }

        @Override
        public AssetRef toAsset() {
            //TODO restrict output stream to a size
            ByteArrayOutputStream output = new ByteArrayOutputStream(
                    10 * 1024 * 1024);
            compressDirectory(localPath, output);
            return assetService.save(new ByteArrayInputStream(output.toByteArray()), "application/x-gzip");
        }
    }

    @Override
    public ApplicationSource createSource(AssetRef assetRef) {
        LOGGER.info("creating source for assetRef {}", assetRef.getUuid());
        try {
            final Path localPath = Files.createTempDirectory(null);
            LOGGER.info("assetRef will be extracted to {}", localPath);
            assetService.handleInputStream(assetRef, new InputStreamHandler() {
                @Override
                public void handleInputStream(InputStream inputStream) {
                    extractFiles(inputStream, localPath);
                }
            });
            return new ApplicationSourceImpl(localPath);
        } catch (IOException e) {
            throw new ApplicationSourceException(e);
        }


    }

    @Override
    public ApplicationSource createSource(AssetRef assetRef, final Path path) {
        LOGGER.info("assetRef {} will be extracted to {}", assetRef.getUuid(), path);
        assetService.handleInputStream(assetRef, new InputStreamHandler() {
            @Override
            public void handleInputStream(InputStream inputStream) {
                extractFiles(inputStream, path);
            }
        });
        return new ApplicationSourceImpl(path);
    }

    @Override
    public ApplicationSource createSource(Path localPath) {
        return new ApplicationSourceImpl(localPath);
    }

    private ApplicationConfig getApplicationConfigInner(Path sourcePath) {
        try {
            LOGGER.info("extracting appConfig");
            DomEditor configEditor = new DomEditor(sourcePath);
            DomEditor.IconConfig iconConfig = configEditor.getIconConfig();
            getIconAssetRef(sourcePath, iconConfig);
            return new ApplicationConfig(configEditor.getName(),
                    configEditor.getPackage(), configEditor.getVersion(),
                    configEditor.getPhoneGapVersion(), configEditor.getFeatures(), iconConfig == null ? null : iconConfig.getHeight(), iconConfig == null ? null : iconConfig.getWidth(), getIconAssetRef(sourcePath, iconConfig));
        } catch (XPathExpressionException ex) {
            LOGGER.info("could not parse config.xml", ex);
            throw new ApplicationConfigurationException(ex);
        } catch (SAXException e) {
            LOGGER.info("could not parse config.xml", e);
            throw new ApplicationConfigurationException(e);
        } catch (IOException e) {
            LOGGER.info("could not parse config.xml", e);
            throw new ApplicationConfigurationException(e);
        } catch (ParserConfigurationException e) {
            LOGGER.info("could not parse config.xml", e);
            throw new ApplicationConfigurationException(e);
        }
    }

    private boolean isSubPath(Path path, Path parent) {
        if (path == null)
            return false;
        if (path.equals(parent))
            return true;
        else
            return isSubPath(path.getParent(), parent);
    }

    private AssetRef getIconAssetRef(Path sourcePath, DomEditor.IconConfig iconConfig) {
        if (iconConfig == null) return null;
        Path path = sourcePath.resolve(iconConfig.getSrc()).normalize();
        if (isSubPath(path, sourcePath) && !path.equals(sourcePath) && path.getFileName().toString().toLowerCase().endsWith(".png")) {
            try (InputStream fs = new FileInputStream(path.toFile())) {
                if (iconConfig.getHeight() != null || iconConfig.getWidth() != null) {
                    BufferedImage bufferedImage = ImageIO.read(fs);
                    bufferedImage = scaleTo(bufferedImage, iconConfig.getHeight(), iconConfig.getWidth());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "png", outputStream);
                    return assetService.save(new ByteArrayInputStream(outputStream.toByteArray()), "image/png");
                } else
                    return assetService.save(fs, "image/png");
            } catch (IOException e) {
                LOGGER.warn("image file cannot be read");
            }
        }
        return null;
    }

    public static BufferedImage scaleTo(BufferedImage image, Integer height, Integer width) throws IOException {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double) (width == null ? 1 : width / imageWidth);
        double scaleY = (double) (height == null ? 1 : height / imageHeight);
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(
                image,
                new BufferedImage(width, height, image.getType()));
    }

}
