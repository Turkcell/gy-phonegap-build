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
package com.ttech.cordovabuild.domain.application;

import com.ttech.cordovabuild.domain.application.ApplicationRepository;
import com.ttech.cordovabuild.domain.user.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationRepositoryImpl implements ApplicationRepository {

    @Autowired
    EntityManager em;

    @Override
    public List<Application> getApplications(User owner) {
        TypedQuery<Application> query = em.createQuery("select a from Application a where a.owner.id:=ownerID", Application.class);
        query.setParameter("ownerID", owner.getId());
        return query.getResultList();
    }

    @Override
    public Application findById(Long id) {
        return em.find(Application.class, id);
    }

    @Override
    public Application saveApplication(Application application) {
        em.persist(application);
        return application;
    }
}
