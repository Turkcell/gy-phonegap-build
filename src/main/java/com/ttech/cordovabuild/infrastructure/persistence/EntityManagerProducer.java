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

package com.ttech.cordovabuild.infrastructure.persistence;

import org.apache.deltaspike.jpa.api.entitymanager.PersistenceUnitName;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagerProducer {


    @Inject
    @PersistenceUnitName("cordova")
    private EntityManagerFactory emf;

    @Produces
    @TransactionScoped
    protected EntityManager createEntityManager() {
        return this.emf.createEntityManager();
    }

    protected void closeEntityManager(@Disposes EntityManager entityManager) {
        if (entityManager.isOpen()) {
            entityManager.close();
        }
    }


}
