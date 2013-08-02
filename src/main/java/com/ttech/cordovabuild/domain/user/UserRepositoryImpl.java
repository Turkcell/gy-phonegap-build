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

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@Autowired
	EntityManager em;

	@Override
	public User findUserByID(Long id) {
		return em.find(User.class, id);
	}

	@Override
	public User findUserByUserName(String username) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		return em.createQuery(
				cq.select(root).where(
						cb.equal(root.get(User_.username), username)))
				.getSingleResult();
	}

	@Override
	public User findUserByEmail(String email) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> root = cq.from(User.class);
		return em.createQuery(
				cq.select(root).where(cb.equal(root.get(User_.email), email)))
				.getSingleResult();
	}

	@Override
	public User saveOrUpdateUser(User user) {
		try {
			em.persist(user);
			return user;
		} catch (EntityExistsException e) {
			return em.merge(user);
		}
	}
}
