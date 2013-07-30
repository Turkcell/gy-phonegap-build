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
package com.ttech.cordovabuild.domain.template;

import com.ttech.cordovabuild.domain.Application;
import com.ttech.cordovabuild.domain.BuildInfo;
import com.ttech.cordovabuild.domain.user.User;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:hazelcastContext.xml", "classpath:datasourceContext.xml", "classpath:applicationContext.xml"})
public class BuildTemplateGeneratorTest {

    @Value("${build.path}")
    private String buildPath;
    @Value("${create.path}")
    private String createPath;

    @Autowired
    BuildTemplateGenerator generator;

    @Test
    public void checkBuildPath() throws IOException, InterruptedException {
        assertNotNull(buildPath);
        assertTrue(!"build.path".equals(buildPath));
    }

    @Test
    public void checkCreateExistence() throws IOException, InterruptedException {
        File file = new File(createPath);
        assertTrue(file.exists());
        assertTrue(file.canExecute());
    }

    @Test
    public void testTemplateCreation() {
        BuildInfo buildInfo = new BuildInfo("http://github.com");
        final Application application = new Application("myapp", new User("Anil"));
        BuildTemplate template = generator.generateTemplate(application, buildInfo);
        File path = new File(new File(buildPath, application.getOwner().getName()), application.getName());
        assertEquals(path.getAbsolutePath(), template.getPath());
    }
}
