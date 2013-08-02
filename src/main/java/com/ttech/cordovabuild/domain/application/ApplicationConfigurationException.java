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

import com.ttech.cordovabuild.domain.CordovaException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

/**
 *
 * @author capacman
 */
public class ApplicationConfigurationException extends CordovaException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 944278372985832460L;
	public ApplicationConfigurationException(ParserConfigurationException ex) {
        super(ex);
    }
    public ApplicationConfigurationException(IOException ex) {
        super(ex);
    }
    public ApplicationConfigurationException(SAXException ex){
        super(ex);
    }
    public ApplicationConfigurationException(XPathExpressionException ex){
        super(ex);
    }
}
