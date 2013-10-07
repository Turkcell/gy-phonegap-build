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

import com.google.common.primitives.Ints;
import com.ttech.cordovabuild.domain.application.ApplicationConfigurationException;
import com.ttech.cordovabuild.domain.application.ApplicationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * Created with IntelliJ IDEA. User: capacman Date: 8/21/13 Time: 7:29 PM To
 * change this built use File | Settings | File Templates.
 */
public class DomEditor {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DomEditor.class);
    private final XPathExpression idExpr;
    private final XPathExpression versionExpr;
    private final XPathExpression nameExpr;
    private final XPathExpression pgExpr;
    private final XPathExpression featureExpr;
    private final XPathExpression iconExpr;
    private final Document doc;

    public DomEditor(Document doc) {
        try {
            this.doc = doc;
            XPath xpath = XPathFactory.newInstance().newXPath();
            idExpr = xpath.compile("/*/@id");
            versionExpr = xpath.compile("/*/@version");
            nameExpr = xpath.compile("/widget/name");
            pgExpr = xpath.compile("/widget/preference/@value");
            featureExpr = xpath.compile("/widget/feature/@name");
            iconExpr = xpath.compile("/widget/icon");
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

    public IconConfig getIconConfig() throws XPathExpressionException {
        Node icon = (Node) iconExpr.evaluate(doc, XPathConstants.NODE);
        if (icon == null)
            return null;
        String src = icon.getAttributes().getNamedItem("src") == null ? null : icon.getAttributes().getNamedItem("src").getNodeValue();
        if (src == null)
            return null;
        Integer width = icon.getAttributes().getNamedItem("width") == null ? null : Ints.tryParse(icon.getAttributes().getNamedItem("width").getNodeValue());
        Integer height = icon.getAttributes().getNamedItem("height") == null ? null : Ints.tryParse(icon.getAttributes().getNamedItem("height").getNodeValue());
        return new IconConfig(src, width, height);
    }

    public void setName(String name) throws XPathExpressionException {
        Node node = (Node) nameExpr.evaluate(doc, XPathConstants.NODE);
        node.setTextContent(name);
    }

    public Set<ApplicationFeature> getFeatures() throws XPathExpressionException {
        NodeList features = (NodeList) featureExpr.evaluate(doc, XPathConstants.NODESET);
        if (features.getLength() < 1)
            return Collections.emptySet();
        List<String> values = new ArrayList<>(features.getLength());
        for (int i = 0; i < features.getLength(); i++)
            values.add(features.item(i).getNodeValue());
        Set<ApplicationFeature> applicationFeatures = new HashSet<>();
        for (String featureValue : values)
            try {
                applicationFeatures.add(ApplicationFeature.fromValue(featureValue));
            } catch (IllegalArgumentException e) {
                LOGGER.info("ignore unknown feature {}", featureValue);
            }
        return applicationFeatures;
    }

    class IconConfig {
        final private String src;
        final private Integer height;
        final private Integer width;

        private IconConfig(String src, Integer height, Integer width) {
            this.src = src;
            this.height = height;
            this.width = width;
        }

        public String getSrc() {
            return src;
        }

        public Integer getHeight() {
            return height;
        }

        public Integer getWidth() {
            return width;
        }
    }
}
