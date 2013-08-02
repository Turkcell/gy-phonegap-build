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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;
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
public class ApplicationTest {

	@Autowired
	ApplicationRepository repository;
	@Autowired
	ApplicationService applicationService;
	@Autowired
	UserRepository userRepository;

	@Test
	public void testRepository() {
		Assert.assertNotNull(repository);
	}

	@Test
	@Transactional
	@Rollback(false)
	public void testCreateApplication() {
		User user = new User("anil", "halil", "achalil@gmail.com", "capacman",
				new ImmutableSet.Builder<Role>().add(Role.USER).build(),
				"passowrd");
		user=userRepository.saveOrUpdateUser(user);
		Application application = applicationService.createApplication("sourceURI", user);
		assertNotNull(application.getId());
		assertNotNull(applicationService.findApplication(application.getId()).getOwner());
	}
	
	@Test
	public void testUpdateApplication(){
		
	}

}
