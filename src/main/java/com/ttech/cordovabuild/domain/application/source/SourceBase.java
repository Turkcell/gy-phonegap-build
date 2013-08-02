package com.ttech.cordovabuild.domain.application.source;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.ttech.cordovabuild.domain.application.ApplicationConfig;
import com.ttech.cordovabuild.domain.application.ApplicationConfigurationException;

public abstract class SourceBase implements ApplicationSource {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SourceBase.class);
	private Path localPath;

	public SourceBase(Path localPath) {
		this.localPath = localPath;
	}

	@Override
	public ApplicationConfig getApplicationData() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(Paths.get(localPath.toString(),
					"config.xml").toFile());
			XPath xpath = XPathFactory.newInstance().newXPath();
			XPathExpression idExpr = xpath.compile("/*/@id");
			XPathExpression versionExpr = xpath.compile("/*/@version");
			XPathExpression nameExpr = xpath.compile("/widget/name");
			String id = idExpr.evaluate(doc);
			String version = versionExpr.evaluate(doc);
			String name = nameExpr.evaluate(doc);
			return new ApplicationConfig(id, version, name);
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
	public Path getLocalPath() {
		return localPath;
	}

}
