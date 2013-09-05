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

package com.ttech.cordovabuild.infrastructure.queue;

import com.hazelcast.core.HazelcastInstance;
import com.ttech.cordovabuild.domain.application.ApplicationBuilt;
import com.ttech.cordovabuild.domain.application.BuiltType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/5/13
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class BuiltQueuePublisherImpl implements BuiltQueuePublisher {
    @Autowired
    HazelcastInstance instance;
    @Value("${build.queue.prefix}")
    String builtQueuePrefix;

    @Override
    public void publishBuilt(ApplicationBuilt built) {
        //TODO implement recovery for queue full
        for (BuiltType builtType : BuiltType.values())
            instance.getQueue(builtQueuePrefix + "." + builtType.getPlatformString()).offer(built.getId());
    }
}
