
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
import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import com.ttech.cordovabuild.domain.application.ApplicationBuilt;

import java.nio.file.Paths;
import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:hazelcastContext.xml", "classpath:datasourceContext.xml", "classpath:applicationContext.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class HazelcastTest {

    @Autowired
    private HazelcastInstance hi;

    @Autowired
    ApplicationContext context;

    @Value("${build.queue.prefix}")
    String queuePrefix;

    @Test
    public void testHazelcastInstance() {
        assertNotNull(hi);
    }

    @Test
    public void testQueue() throws InterruptedException {
        IQueue<Object> queue = hi.getQueue(queuePrefix + ".test");
        assertNotNull(queue);
        queue.put(new ApplicationBuilt());
        assertNotNull(queue.poll());
        System.out.println(Paths.get("").toAbsolutePath());
    }

}
