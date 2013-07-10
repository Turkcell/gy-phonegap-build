
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
import com.ttech.cordovabuild.domain.BuildInfo;
import com.ttech.cordovabuild.infrastructure.CustomPropertyFileConfig;
import org.apache.commons.dbcp.BasicDataSource;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;

import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class HazelcastTest {

    @Inject
    private HazelcastInstance hi;
    @Inject
    @BuildQueue
    private IQueue<BuildInfo> queue;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive jar = ShrinkWrap.create(WebArchive.class, "ROOT.war")
                .addPackages(true, HazelcastTest.class.getPackage())
                .addClass(CustomPropertyFileConfig.class)
                .addAsWebResource("cordova.properties")
                .addAsDirectory("src/main/resources/META-INF")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
        System.out.println(jar.toString(true));
        return jar;
    }

    @Test
    public void testHazelcastInstance() {
        assertNotNull(hi);
        System.out.println(BasicDataSource.class.getName());
    }

    @Test
    public void testQueue() throws InterruptedException {
        assertNotNull(queue);
        queue.put(new BuildInfo("http://github.com"));
        assertNotNull(queue.poll());
    }
}