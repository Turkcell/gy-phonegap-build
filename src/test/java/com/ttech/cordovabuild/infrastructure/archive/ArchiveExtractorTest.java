
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

package com.ttech.cordovabuild.infrastructure.archive;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;


@RunWith(Arquillian.class)
public class ArchiveExtractorTest {

    @Inject
    ArchiveExtractor ae;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive jar = ShrinkWrap.create(WebArchive.class, "ROOT.war")
                .addPackage(ArchiveExtractor.class.getPackage())
                .addAsWebResource("cordova.properties")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
        System.out.println(jar.toString(true));
        return jar;
    }

    @Test
    public void checkBuildPath() throws IOException, InterruptedException {
        assertNotNull(ae);
        ae.extractArchive(new BufferedInputStream(new FileInputStream("/home/capacman/Data/Downloads/easyXDM-2.4.17.1.zip")), new File("/home/capacman/tmp/extract1"));
    }
}
