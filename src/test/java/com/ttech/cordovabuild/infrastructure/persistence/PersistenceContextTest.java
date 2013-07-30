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

import com.ttech.cordovabuild.domain.asset.Asset;
import com.ttech.cordovabuild.domain.asset.AssetRepository;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.naming.NamingException;
import javax.sql.DataSource;
import static junit.framework.Assert.assertEquals;

import static junit.framework.Assert.assertNotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:datasourceContext.xml","classpath:applicationContext.xml"})
@TransactionConfiguration(transactionManager = "tx")
public class PersistenceContextTest {

    @Autowired
    AssetRepository assetRepository;
    @Autowired
    DataSource datasource;

    @Test
    public void testDataSource() throws NamingException {
        assertNotNull(datasource);
    }

    @Test
    @Transactional
    public void testAssetRepository() {
        assertNotNull(assetRepository);
        Asset a = new Asset();
        a.setData("test".getBytes());
        assetRepository.save(a);
        assertNotNull(a.getId());
        assertNotNull(assetRepository.findByID(a.getId()));
    }

    @Test
    @Transactional
    public void testPrevious() {
        assertNotNull(assetRepository);
        assertEquals(0, assetRepository.getAll().size());
    }
}
