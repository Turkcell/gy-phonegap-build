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

import com.ttech.cordovabuild.domain.user.User;
import com.ttech.cordovabuild.domain.user.User_;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ApplicationRepositoryImpl implements ApplicationRepository {

    @Autowired
    EntityManager em;

    @Override
    public List<ApplicationBuilt> getApplications(User owner) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ApplicationBuilt> cq = cb.createQuery(ApplicationBuilt.class);
        Root<ApplicationBuilt> from = cq.from(ApplicationBuilt.class);
        return em.createQuery(cq.select(from).where(cb.equal(from.get(ApplicationBuilt_.application).get(Application_.owner), owner))).getResultList();
    }

    @Override
    public Application findById(Long id) {
        Application application = em.find(Application.class, id);
        if (application == null)
            throw new ApplicationNotFoundException(id);
        return application;
    }

    @Override
    public Application saveApplication(Application application) {
        em.persist(application);
        return application;
    }

    @Override
    public Application updateApplication(Application application) {
        return em.merge(application);
    }

    @Override
    public ApplicationBuilt saveApplicationBuilt(ApplicationBuilt applicationBuilt) {
        em.persist(applicationBuilt);
        return applicationBuilt;
    }

    @Override
    public ApplicationBuilt findApplicationBuild(Long id) {
        ApplicationBuilt applicationBuilt = em.find(ApplicationBuilt.class, id);
        if (applicationBuilt == null)
            throw new ApplicationBuiltNotFoundException(id);
        return applicationBuilt;
    }

    @Override
    public ApplicationBuilt updateApplicationBuilt(ApplicationBuilt applicationBuilt) {
        return em.merge(applicationBuilt);
    }

    @Override
    public ApplicationBuilt getLatestBuilt(Application application) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ApplicationBuilt> cq = cb.createQuery(ApplicationBuilt.class);
        Root<ApplicationBuilt> from = cq.from(ApplicationBuilt.class);
        cq = cq.where(cb.equal(from.get(ApplicationBuilt_.application), application)).orderBy(cb.desc(from.get(ApplicationBuilt_.startDate)));
        try {
            return em.createQuery(cq).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            throw new ApplicationBuiltNotFoundException(e);
        }
    }
}
