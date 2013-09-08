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
package com.ttech.cordovabuild.domain.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableSet;

/**
 * 
 * @author Anıl Halil
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:hazelcastContext.xml",
		"classpath:datasourceContext.xml", "classpath:applicationContext.xml" })
@TransactionConfiguration(transactionManager = "tx")
public class UserTest {

	private static final String EMAIL = "achalil@gmail.com";
	@Autowired
	UserRepository userRepository;

	@Test
	@Transactional
	public void testCreateUser() {
		User user = new User("anil", "halil", EMAIL, "capacman",
				new ImmutableSet.Builder<Role>().add(Role.ROLE_USER).build(),
				"passowrd");
		user = userRepository.saveOrUpdateUser(user);
		assertNotNull(user.getId());
		assertEquals(user,userRepository.findUserByEmail(EMAIL));
		assertEquals(user,userRepository.findUserByID(user.getId()));
		assertEquals(user,userRepository.findUserByUserName("capacman"));
	}
}
