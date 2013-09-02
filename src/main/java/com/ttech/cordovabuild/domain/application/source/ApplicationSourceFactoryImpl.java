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
import com.ttech.cordovabuild.domain.application.ApplicationFeature;
import com.ttech.cordovabuild.domain.asset.Asset;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.ttech.cordovabuild.infrastructure.archive.ArchiveUtils.compressDirectory;
import static com.ttech.cordovabuild.infrastructure.archive.ArchiveUtils.extractFiles;

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
        LOGGER.info("creating source for asset{}",asset.getId());
		Path localPath = null;
		try {
			localPath = Files.createTempDirectory(null);
		} catch (IOException e) {
			throw new ApplicationSourceException(e);
		}
        LOGGER.info("asset will be extracted to {}",localPath);
		extractFiles(asset.asInputStream(), localPath);
		return new ApplicationSourceImpl(localPath);

	}

	@Override
	public ApplicationSource createSource(Path localPath) {
		return new ApplicationSourceImpl(localPath);
	}

	@Override
	public Asset toAsset(ApplicationSource applicationSource) {
		ByteArrayOutputStream output = new ByteArrayOutputStream(
				10 * 1024 * 1024);
		compressDirectory(applicationSource.getLocalPath(), output);
		return new Asset(output.toByteArray());
	}

	private ApplicationConfig getApplicationConfigInner(Path sourcePath) {
		try {
            LOGGER.info("extracting appConfig");
			DomEditor configEditor = new DomEditor(sourcePath);
			return new ApplicationConfig(configEditor.getName(),
					configEditor.getPackage(), configEditor.getVersion(),
					configEditor.getPhoneGapVersion(),configEditor.getFeatures());
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
}
