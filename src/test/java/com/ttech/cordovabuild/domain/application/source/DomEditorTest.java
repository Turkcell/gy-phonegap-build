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

import org.junit.Assert;
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
 * To change this template use File | Settings | File Templates.
 */
public class DomEditorTest {

    @Test
    public void testEditor() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        DomEditor domEditor = new DomEditor(Paths.get("src/test/resources"));
        assertEquals(domEditor.getName(), "RestaurantReviews");
        assertEquals(domEditor.getPackage(), "com.turkcell.restaurantee");
        assertEquals(domEditor.getVersion(), "1.0.0");
        assertEquals(domEditor.getPhoneGapVersion(), "2.5.0");

        domEditor.setName("name");
        domEditor.setPackage("package");
        domEditor.setVersion("version");
        domEditor.setPhoneGapVersion("pgversion");

        assertEquals(domEditor.getName(), "name");
        assertEquals(domEditor.getPackage(), "package");
        assertEquals(domEditor.getVersion(), "version");
        assertEquals(domEditor.getPhoneGapVersion(), "pgversion");
    }
}
