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
import com.ttech.cordovabuild.domain.application.BuiltType;
import com.ttech.cordovabuild.domain.application.Application;
import com.ttech.cordovabuild.domain.application.ApplicationService;
import com.ttech.cordovabuild.infrastructure.queue.QueueListener;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/1/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuildService implements QueueListener<Long>{
    private BuiltType builtType;

    @Autowired
    ApplicationBuilder templateGenerator;
    @Autowired
    ApplicationService applicationService;
    @Override
    public void onItem(Long itemId) {
        ApplicationBuilt applicationBuilt = applicationService.findApplicationBuild(itemId);
        Application application = applicationService.findByApplicationBuild(applicationBuilt);
        BuildInfo buildInfo = templateGenerator.buildApplication(application, applicationBuilt,builtType);

    }
}
