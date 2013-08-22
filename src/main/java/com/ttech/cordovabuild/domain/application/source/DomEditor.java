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

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.ttech.cordovabuild.domain.application.ApplicationConfigurationException;

/**
 * Created with IntelliJ IDEA. User: capacman Date: 8/21/13 Time: 7:29 PM To
 * change this template use File | Settings | File Templates.
 */
public class DomEditor {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DomEditor.class);
	private final XPathExpression idExpr;
	private final XPathExpression versionExpr;
	private final XPathExpression nameExpr;
	private final XPathExpression pgExpr;
	private final Document doc;

	public DomEditor(Document doc) {
		try {
			this.doc = doc;
			XPath xpath = XPathFactory.newInstance().newXPath();
			idExpr = xpath.compile("/*/@id");
			versionExpr = xpath.compile("/*/@version");
			nameExpr = xpath.compile("/widget/name");
			pgExpr = xpath.compile("/widget/preference/@value");
		} catch (XPathExpressionException ex) {
			LOGGER.info("could not parse config.xml", ex);
			throw new ApplicationConfigurationException(ex);
		}
	}

	public DomEditor(Path sourcePath) throws SAXException, IOException,
			ParserConfigurationException {
		this(DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(sourcePath.resolve("config.xml").toFile()));
	}

	public String getPhoneGapVersion() throws XPathExpressionException {
		return pgExpr.evaluate(doc);
	}

	public void setPhoneGapVersion(String version)
			throws XPathExpressionException {
		setNodeValue(pgExpr, version);
	}

	private void setNodeValue(XPathExpression expr, String version)
			throws XPathExpressionException {
		Node node = (Node) expr.evaluate(doc, XPathConstants.NODE);
		node.setNodeValue(version);
	}

	public String getVersion() throws XPathExpressionException {
		return versionExpr.evaluate(doc);
	}

	public void setVersion(String version) throws XPathExpressionException {
		setNodeValue(versionExpr, version);
	}

	public String getPackage() throws XPathExpressionException {
		return idExpr.evaluate(doc);
	}

	public void setPackage(String packageVal) throws XPathExpressionException {
		setNodeValue(idExpr, packageVal);
	}

	public String getName() throws XPathExpressionException {
		return nameExpr.evaluate(doc);
	}

	public void setName(String name) throws XPathExpressionException {
		Node node = (Node) nameExpr.evaluate(doc, XPathConstants.NODE);
		node.setTextContent(name);
	}
}