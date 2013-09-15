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

import com.google.common.collect.Sets;
import com.ttech.cordovabuild.domain.application.source.ApplicationSource;
import com.ttech.cordovabuild.domain.application.source.ApplicationSourceFactory;
import com.ttech.cordovabuild.domain.asset.AssetRef;
import com.ttech.cordovabuild.domain.built.BuiltInfo;
import com.ttech.cordovabuild.domain.user.Role;
import com.ttech.cordovabuild.domain.user.User;
import com.ttech.cordovabuild.domain.user.UserRepository;
import com.ttech.cordovabuild.infrastructure.git.GitUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import java.nio.file.Path;

import static org.junit.Assert.*;

/**
 * @author capacman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:hazelcastContext.xml",
        "classpath:datasourceContext.xml", "classpath:applicationContext.xml"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ApplicationServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceTest.class);
    public static final String GIT_REPO = "https://github.com/Turkcell/RestaurantReviews.git";

    @Autowired
    ApplicationService applicationService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ApplicationSourceFactory sourceFactory;
    private static Path clonePath;
    @Autowired
    EntityManager entityManager;
    private static User user;

    @BeforeClass
    public static void cloneRepo() {
        clonePath = GitUtils.clone(GIT_REPO);
    }

    @Test
    public void testAssetCreate() {
        AssetRef assetRef = createAsset();
        ApplicationSource source = sourceFactory.createSource(assetRef);
        assertNotNull(source);
        assertTrue(source.getLocalPath().toFile().exists());
        assertTrue(source.getLocalPath().resolve("config.xml").toFile().exists());
    }

    @Test
    public void testApplicationURI() {
        Application appWithURI = new Application();
        appWithURI.setRepositoryURI("testuri");
        assertTrue(appWithURI.hasRepositoryUri());
        Application appWithNullURL = new Application();
        assertFalse(appWithNullURL.hasRepositoryUri());
        appWithNullURL.setRepositoryURI("");
        assertFalse(appWithNullURL.hasRepositoryUri());
    }

    private AssetRef createAsset() {
        return sourceFactory.createSource(clonePath).toAsset();
    }

    @Test
    public void testCreateApplicationWithRepo() {
        Application application = applicationService.createApplication(createUser(), GIT_REPO);
        assertNotNull(application.getId());
        assertNotNull(application.getCreated());
        assertNotNull(application.getOwner());
        assertNotNull(application.getRepositoryURI());
        assertNotNull(applicationService.findApplication(application.getId()).getOwner());
    }

    private User createUser() {
        if (user == null) {
            user = new User("anil", "halil", "achalil@gmail.com", "capacman",
                    Sets.newHashSet(Role.ROLE_USER),
                    "passowrd");
            user = userRepository.saveOrUpdateUser(user);
        }
        return user;
    }

    @Test
    public void testCreateApplicationWithAsset() {
        AssetRef assetRef = createAsset();
        Application application = applicationService.createApplication(createUser(), assetRef);
        assertNotNull(application);
    }

    @Test
    public void testApplicationConfig() {
        AssetRef assetRef = createAsset();
        Application application = applicationService.createApplication(createUser(), assetRef);
        assertNotNull(application);
        assertNotNull(application.getId());
        ApplicationConfig config = application.getApplicationConfig();
        ApplicationBuilt applicationBuilt = applicationService.prepareApplicationBuilt(application);
        assertFalse(config == applicationBuilt.getBuiltConfig());
        config.setApplicationName("test");
        assertFalse(config.getApplicationName().equals(applicationService.findApplicationBuilt(applicationBuilt.getId()).getBuiltConfig().getApplicationName()));
    }

    @Test(expected = ApplicationNotFoundException.class)
    public void testNotFoundApplication() {
        applicationService.findApplication(-1L);
    }


    @Test
    public void testApplicationBuilt() {
        AssetRef asset = createAsset();
        Application application = applicationService.createApplication(createUser(), asset);
        ApplicationBuilt applicationBuilt = applicationService.prepareApplicationBuilt(application);
        assertNotNull(applicationBuilt.getId());
        assertEquals(applicationBuilt.getApplication(), application);
        assertNotNull(applicationBuilt.getStartDate());
    }


    @Test(expected = OptimisticLockException.class)
    public void testApplicationBuiltConcurrency() {
        AssetRef asset = createAsset();
        Application application = applicationService.createApplication(createUser(), asset);
        ApplicationBuilt androidBuilt = applicationService.prepareApplicationBuilt(application);
        ApplicationBuilt iosBuilt = applicationService.findApplicationBuilt(androidBuilt.getId());
        applicationService.updateApplicationBuilt(androidBuilt.update(BuiltInfo.failedFor(application.getApplicationConfig().getApplicationName(), BuiltType.ANDROID)));
        applicationService.updateApplicationBuilt(iosBuilt.update(BuiltInfo.failedFor(application.getApplicationConfig().getApplicationName(), BuiltType.IOS)));
    }

    @Test
    public void testUpdateApplicationSource() {
        Application application = applicationService.createApplication(createUser(), GIT_REPO);
        assertNotNull(application.getSourceAssetRef());
        Application updatedApplication = applicationService.updateApplicationCode(application.getId());
        assertNotNull(updatedApplication.getSourceAssetRef());
        assertFalse(application.getSourceAssetRef().equals(updatedApplication.getSourceAssetRef()));
    }

    @Test(expected = ApplicationUpdateException.class)
    public void testApplicationUpdateExceptionWithURI() {
        Application application = applicationService.createApplication(createUser(), GIT_REPO);
        applicationService.updateApplicationCode(application.getId(), new AssetRef());
    }

    @Test(expected = ApplicationUpdateException.class)
    public void testApplicationUpdateExceptionWithOutURI() {
        AssetRef asset = createAsset();
        Application application = applicationService.createApplication(createUser(), asset);
        applicationService.updateApplicationCode(application.getId());
    }

    @Test
    public void testLatestBuilt() {
        Application application = applicationService.createApplication(createUser(), GIT_REPO);
        applicationService.prepareApplicationBuilt(application);
        ApplicationBuilt lastBuilt = applicationService.prepareApplicationBuilt(application);
        assertEquals(lastBuilt.getId(), applicationService.getLatestBuilt(application).getId());
    }

    @Test
    public void testWaitingApplicationBuilt() {
        Application application = applicationService.createApplication(createUser(), GIT_REPO);
        ApplicationBuilt applicationBuilt = applicationService.prepareApplicationBuilt(application);
        assertEquals(BuiltType.values().length, applicationBuilt.getBuiltTargets().size());
        for (BuiltTarget builtTarget : applicationBuilt.getBuiltTargets()) {
            assertEquals(BuiltTarget.Status.WAITING, builtTarget.getStatus());
        }
    }

}
