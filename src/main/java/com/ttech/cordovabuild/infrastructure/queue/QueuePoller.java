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

import com.hazelcast.core.HazelcastInstanceNotActiveException;
import com.hazelcast.core.IQueue;
import com.ttech.cordovabuild.domain.application.ApplicationBuilt;
import com.ttech.cordovabuild.domain.application.ApplicationService;
import com.ttech.cordovabuild.domain.application.BuiltType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/5/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueuePoller implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueuePoller.class);
    private final QueueListener listener;
    private final ApplicationService applicationService;
    private final BuiltType builtType;
    private final IQueue<Long> queue;

    public QueuePoller(QueueListener listener, ApplicationService applicationService, BuiltType builtType, IQueue<Long> queue) {
        this.listener = listener;
        this.applicationService = applicationService;
        this.builtType = builtType;
        this.queue = queue;
    }

    @Override
    public void run() {
        Long builtID = null;
        while (true) {
            try {
                builtID = queue.poll(1000, TimeUnit.MILLISECONDS);
                if (builtID != null) {
                    ApplicationBuilt built = applicationService.findApplicationBuilt(builtID);
                    listener.onBuilt(built, builtType);
                }
            } catch (InterruptedException e) {
                LOGGER.info("queue polling for {} has been interrupted", e);
                break;
            } catch (HazelcastInstanceNotActiveException e) {
                LOGGER.warn("hazelcast is going down stop threads");
                break;
            } catch (DataAccessException e) {
                LOGGER.error("could not get applicationBuilt for {}", builtID, e);
                //TODO implement built failed
            } catch (Exception e) {
                LOGGER.error("unexpected error", e);
            }
        }
    }
}
