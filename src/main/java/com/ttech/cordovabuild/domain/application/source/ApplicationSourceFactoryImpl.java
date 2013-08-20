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

import com.google.common.io.Files;
import com.ttech.cordovabuild.domain.application.ApplicationConfig;
import com.ttech.cordovabuild.domain.application.ApplicationConfigurationException;
import com.ttech.cordovabuild.domain.asset.Asset;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ApplicationSourceFactoryImpl implements ApplicationSourceFactory {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ApplicationSourceFactoryImpl.class);

    public class ApplicationSourceImpl implements ApplicationSource {

        private Path localPath;

        public ApplicationSourceImpl(Path localPath) {
            this.localPath = localPath;
        }

        @Override
        public Path getLocalPath() {
            return localPath;
        }

        @Override
        public ApplicationConfig getApplicationConfig() {
            return getApplicationConfigInner(localPath);
        }
    }

    @Override
    public ApplicationSource createSource(Asset asset) {
        ArchiveStreamFactory factory = new ArchiveStreamFactory();
        Path localPath = Files.createTempDir().toPath();
        try (ArchiveInputStream ais = factory.createArchiveInputStream(asset.asInputStream());) {
            ArchiveEntry ae;
            while ((ae = ais.getNextEntry()) != null) {
                if (ae.isDirectory()) {
                    continue;
                }
                try (FileOutputStream outputStream = FileUtils.openOutputStream(Paths.get(localPath.toString(), ae.getName()).toFile())) {
                    IOUtils.copy(ais, outputStream);
                }
            }
        } catch (ArchiveException e) {
            throw new ArchiveExtractionException(e);
        } catch (IOException e) {
            throw new ArchiveExtractionException(e);
        }
        return new ApplicationSourceImpl(localPath);

    }

    @Override
    public ApplicationSource createSource(Path localPath) {
        return new ApplicationSourceImpl(localPath);
    }

    @Override
    public Asset toAsset(ApplicationSource applicationSource) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private ApplicationConfig getApplicationConfigInner(Path sourcePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(Paths.get(sourcePath.toString(),
                    "config.xml").toFile());
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression idExpr = xpath.compile("/*/@id");
            XPathExpression versionExpr = xpath.compile("/*/@version");
            XPathExpression nameExpr = xpath.compile("/widget/name");
            XPathExpression pgExpr = xpath
                    .compile("/widget/preference/@value");
            return new ApplicationConfig(nameExpr.evaluate(doc),
                    idExpr.evaluate(doc), versionExpr.evaluate(doc),
                    pgExpr.evaluate(doc));
        } catch (ParserConfigurationException ex) {
            LOGGER.error("could not parse config.xml", ex);
            throw new ApplicationConfigurationException(ex);
        } catch (SAXException ex) {
            LOGGER.info("could not parse config.xml", ex);
            throw new ApplicationConfigurationException(ex);
        } catch (IOException ex) {
            LOGGER.info("could not parse config.xml", ex);
            throw new ApplicationConfigurationException(ex);
        } catch (XPathExpressionException ex) {
            LOGGER.info("could not parse config.xml", ex);
            throw new ApplicationConfigurationException(ex);
        }
    }
}
