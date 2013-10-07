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

import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 8/22/13
 * Time: 11:24 PM
 * To change this built use File | Settings | File Templates.
 */
public class DomEditorTest {

    @Test
    public void testEditor() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        DomEditor domEditor = new DomEditor(Paths.get("src/test/resources"));
        assertEquals("RestaurantReviews", domEditor.getName());
        assertEquals("com.turkcell.restaurantee", domEditor.getPackage());
        assertEquals("1.0.0", domEditor.getVersion());
        assertEquals("2.5.0", domEditor.getPhoneGapVersion());
        assertEquals(0, domEditor.getFeatures().size());
        assertEquals("icons/rest_icon_57.png", domEditor.getIconConfig().getSrc());
        assertEquals(57, domEditor.getIconConfig().getWidth());
        assertEquals(57, domEditor.getIconConfig().getHeight());

        domEditor.setName("name");
        domEditor.setPackage("package");
        domEditor.setVersion("version");
        domEditor.setPhoneGapVersion("pgversion");

        assertEquals("name", domEditor.getName());
        assertEquals("package", domEditor.getPackage());
        assertEquals("version", domEditor.getVersion());
        assertEquals("pgversion", domEditor.getPhoneGapVersion());
    }
}
