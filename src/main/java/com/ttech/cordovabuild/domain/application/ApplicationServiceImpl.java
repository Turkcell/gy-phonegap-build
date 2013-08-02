/*
 * Copyright 2013 Turkcell Teknoloji Inc. and individual
 * contributors by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ttech.cordovabuild.domain.application;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.ttech.cordovabuild.domain.application.source.ApplicationSourceFactory;
import com.ttech.cordovabuild.domain.user.User;

/**
 *
 * @author Anıl Halil
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceImpl.class);
    @Autowired
    ApplicationRepository repository;
    @Autowired
    ApplicationSourceFactory sourceFactory;

    @Override
    public Application createApplication(String sourceURI, User owner) {
        Application application = new Application(sourceURI, owner);
        return repository.saveApplication(application);
    }

    @Override
    public Application updateApplicationFromSource(Application application, Path configPath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(configPath.toFile());
            XPath xpath = XPathFactory.newInstance().newXPath();
            XPathExpression idExpr = xpath.compile("/*/@id");
            XPathExpression versionExpr = xpath.compile("/*/@version");
            XPathExpression nameExpr = xpath.compile("/widget/name");
            String id = idExpr.evaluate(doc);
            String version = versionExpr.evaluate(doc);
            String name = nameExpr.evaluate(doc);
            application.setAppPackage(id);
            application.setVersion(version);
            application.setName(name);
            return repository.saveApplication(application);
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

    @Override
    public Application updateApplication(Application application) {
        return repository.saveApplication(application);
    }

	@Override
	public Application findApplication(Long id) {
		return repository.findById(id);
	}

}
