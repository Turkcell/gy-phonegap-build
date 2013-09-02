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

import com.google.common.collect.ImmutableSet;
import com.ttech.cordovabuild.domain.application.ApplicationBuilt;
import com.ttech.cordovabuild.domain.application.Application;
import com.ttech.cordovabuild.domain.application.ApplicationService;
import com.ttech.cordovabuild.domain.application.BuiltType;
import com.ttech.cordovabuild.domain.user.Role;
import com.ttech.cordovabuild.domain.user.User;

import com.ttech.cordovabuild.domain.user.UserRepository;
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
@ContextConfiguration({"classpath:hazelcastContext.xml",
        "classpath:datasourceContext.xml", "classpath:applicationContext.xml"})
public class ApplicationBuilderTest {

    @Value("${build.path}")
    private String buildPath;
    @Value("${create.path}")
    private String createPath;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    UserRepository userRepository;
    public static final String GIT_REPO = "https://github.com/Turkcell/RestaurantReviews.git";
    @Autowired
    ApplicationBuilder generator;

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
        ApplicationBuilt buildInfo = new ApplicationBuilt();
        User user = new User("anil", "halil", "achalil@gmail.com", "capacman",
                new ImmutableSet.Builder<Role>().add(Role.USER).build(),
                "passowrd");
        user = userRepository.saveOrUpdateUser(user);
        Application application = applicationService.createApplication(user, GIT_REPO);
        BuildInfo template = generator.buildApplication(application,
                buildInfo, BuiltType.ANDROID);
        File path = new File(new File(buildPath, application.getOwner()
                .getName()), application.getApplicationConfig().getName());
        assertEquals(path.getAbsolutePath(), template.getPath().toString());
    }
}
