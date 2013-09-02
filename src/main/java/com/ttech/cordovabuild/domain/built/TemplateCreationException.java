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

package com.ttech.cordovabuild.domain.built;

import com.ttech.cordovabuild.domain.CordovaException;

import java.io.IOException;
import java.text.MessageFormat;


public class TemplateCreationException extends CordovaException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6492066447596321028L;

	public TemplateCreationException(IOException e) {
        super(e);
    }

    public TemplateCreationException(int result) {
        super(MessageFormat.format("built creation returns {0,number,integer}",result));
    }

    public TemplateCreationException(InterruptedException e) {
        super(e);
    }

    public TemplateCreationException(String message) {
        super(message);
    }
}
