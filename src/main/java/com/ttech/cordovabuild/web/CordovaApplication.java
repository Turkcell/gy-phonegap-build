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

package com.ttech.cordovabuild.web;

import com.ttech.cordovabuild.web.exception.NotFoundExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/7/13
 * Time: 4:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class CordovaApplication extends ResourceConfig {

    public CordovaApplication() {
        register(RequestContextFilter.class);
        register(RootResource.class);
        register(UserResource.class);
        register(NotFoundExceptionMapper.class);
    }
}
