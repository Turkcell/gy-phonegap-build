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

import com.ttech.cordovabuild.domain.application.ApplicationBuilt;
import com.ttech.cordovabuild.domain.application.BuiltTarget;
import com.ttech.cordovabuild.domain.application.BuiltType;
import com.ttech.cordovabuild.domain.application.source.ApplicationSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/3/13
 * Time: 9:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ApplicationBuilderFactoryImpl implements ApplicationBuilderFactory {

    private Path buildPath;
    @Value("${create.path}")
    private String createPath;

    @Autowired
    ApplicationSourceFactory sourceFactory;

    @Override
    public ApplicationBuilder getApplicationBuilder(BuiltType builtType, ApplicationBuilt applicationBuilt) {
        switch (builtType) {
            case ANDROID:
                return new AndroidApplicationBuilder(buildPath, createPath, sourceFactory, applicationBuilt);
            default:
                throw new IllegalArgumentException("could not find ApplicationBuilder for " + builtType);
        }
    }

    @Value("${build.path}")
    public void setBuildPath(String buildPath) {
        this.buildPath = Paths.get(buildPath);
    }
}
