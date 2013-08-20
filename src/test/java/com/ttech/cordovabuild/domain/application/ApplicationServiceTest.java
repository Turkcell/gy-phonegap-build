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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import org.junit.Assert;
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
import com.google.common.io.Files;
import com.ttech.cordovabuild.domain.application.source.ApplicationSource;
import com.ttech.cordovabuild.domain.application.source.ApplicationSourceFactory;
import com.ttech.cordovabuild.domain.user.Role;
import com.ttech.cordovabuild.domain.user.User;
import com.ttech.cordovabuild.domain.user.UserRepository;

/**
 * 
 * @author capacman
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:hazelcastContext.xml",
		"classpath:datasourceContext.xml", "classpath:applicationContext.xml" })
@TransactionConfiguration(transactionManager = "tx")
public class ApplicationServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationServiceTest.class);
    public static final String GIT_REPO = "https://github.com/Turkcell/RestaurantReviews.git";

    @Autowired
	ApplicationRepository repository;
	@Autowired
	ApplicationService applicationService;
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ApplicationSourceFactory sourceFactory;

	@Test
	public void testRepository() {
		Assert.assertNotNull(repository);
	}

	@Test
	@Transactional
	public void testCreateApplicationWithRepo() {
		User user = new User("anil", "halil", "achalil@gmail.com", "capacman",
				new ImmutableSet.Builder<Role>().add(Role.USER).build(),
				"passowrd");
		user=userRepository.saveOrUpdateUser(user);
		Application application = applicationService.createApplication(user,GIT_REPO);
		assertNotNull(application.getId());
		assertNotNull(applicationService.findApplication(application.getId()).getOwner());
	}
	/*
	@Test
	public void testCreateApplicationWithAsset(){
		File tempDir = Files.createTempDir();
		LOGGER.info("{} is temp dir ",tempDir.getAbsolutePath());
		ApplicationSource source = sourceFactory.createSource(GIT_REPO, tempDir.toPath());
		assertNotNull(source);
		ApplicationConfig applicationData = source.getApplicationConfig();
		LOGGER.info(applicationData.toString());
	}
*/
}
