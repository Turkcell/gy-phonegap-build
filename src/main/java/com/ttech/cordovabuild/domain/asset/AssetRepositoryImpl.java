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
package com.ttech.cordovabuild.domain.asset;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AssetRepositoryImpl implements AssetRepository {

    @Autowired
    EntityManager entityManager;

    @Override
    public Asset findByID(Long id) {
        return entityManager.find(Asset.class, id);
    }

    @Override
    public Asset save(Asset asset) {
        entityManager.persist(asset);
        return asset;
    }

    @Override
    public List<Asset> getAll() {
        CriteriaQuery<Asset> cq = entityManager.getCriteriaBuilder().createQuery(Asset.class);
        Root<Asset> from = cq.from(Asset.class);
        return entityManager.createQuery(cq.select(from)).getResultList();
    }
}
