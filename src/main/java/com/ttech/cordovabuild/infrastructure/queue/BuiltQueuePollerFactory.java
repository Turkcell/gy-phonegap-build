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
import com.hazelcast.core.IQueue;
import com.ttech.cordovabuild.domain.application.ApplicationService;
import com.ttech.cordovabuild.domain.application.BuiltType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/5/13
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class BuiltQueuePollerFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuiltQueuePollerFactory.class);
    @Value("${built.types}")
    String builtTypes;
    @Value("${build.queue.prefix}")
    String builtQueuePrefix;

    @Autowired
    QueueListener queueListener;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    HazelcastInstance hazelcastInstance;

    @PostConstruct
    void buildQueuePollers() {
        LOGGER.info("built.types value {} and builtQueue prefix {}",builtTypes,builtQueuePrefix);
        String[] split = builtTypes.split(",");
        for (String splitVal : split) {
            BuiltType builtType = BuiltType.valueOf(splitVal);
            IQueue<Long> queue = hazelcastInstance.getQueue(builtQueuePrefix + "." + builtType.getPlatformString());
            Thread pollerThread = new Thread((new QueuePoller(queueListener, applicationService, builtType, queue)));
            pollerThread.setName(builtType.getPlatformString() + "BuiltPoller");
            pollerThread.start();
        }
    }
}
