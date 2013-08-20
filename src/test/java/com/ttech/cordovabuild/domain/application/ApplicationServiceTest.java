/*
 * Copyright 2013 Turkcell Teknoloji Inc. and individual
 * contributors by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ttech.cordovabuild.domain.application;

import static com.ttech.cordovabuild.infrastructure.git.GitUtils.clone;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;

import com.ttech.cordovabuild.domain.asset.Asset;
import com.ttech.cordovabuild.infrastructure.git.GitUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;
import com.ttech.cordovabuild.domain.application.source.ApplicationSource;
import com.ttech.cordovabuild.domain.application.source.ApplicationSourceFactory;
import com.ttech.cordovabuild.domain.user.Role;
import com.ttech.cordovabuild.domain.user.User;
import com.ttech.cordovabuild.domain.user.UserRepository;

/**
 * @author capacman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:hazelcastContext.xml",
        "classpath:datasourceContext.xml", "classpath:applicationContext.xml"})
@TransactionConfiguration(transactionManager = "tx")
public class ApplicationServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceTest.class);
    public static final String GIT_REPO = "https://github.com/Turkcell/RestaurantReviews.git";

    @Autowired
    ApplicationService applicationService;
    @Autowired
    UserRepository userRepository;

    @Autowired
    ApplicationSourceFactory sourceFactory;

    @Test
    public void testAssetCreate() {
        Asset asset = createAsset();
        assertNotNull(asset);
        assertNotNull(asset.getData());
        assertTrue(asset.getData().length>0);
        ApplicationSource source = sourceFactory.createSource(asset);
        assertNotNull(source);
        assertTrue(source.getLocalPath().toFile().exists());
        assertTrue(source.getLocalPath().resolve("config.xml").toFile().exists());
    }

    private Asset createAsset() {
        Path localPath = GitUtils.clone(GIT_REPO);
        ApplicationSource source = sourceFactory.createSource(localPath);
        return sourceFactory.toAsset(source);
    }

    @Test
    @Transactional
    public void testCreateApplicationWithRepo() {
        User user = createUser();
        user = userRepository.saveOrUpdateUser(user);
        Application application = applicationService.createApplication(user, GIT_REPO);
        assertNotNull(application.getId());
        assertNotNull(applicationService.findApplication(application.getId()).getOwner());
    }

    private User createUser() {
        return new User("anil", "halil", "achalil@gmail.com", "capacman",
                    new ImmutableSet.Builder<Role>().add(Role.USER).build(),
                    "passowrd");
    }

    @Test
    public void testCreateApplicationWithAsset() {
        Asset asset = createAsset();
        User user = createUser();
        userRepository.saveOrUpdateUser(user);
        Application application = applicationService.createApplication(user, asset);
        assertNotNull(application);
    }

}
