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

import org.apache.deltaspike.jpa.spi.entitymanager.PersistenceConfigurationProvider;
import org.eclipse.persistence.config.PersistenceUnitProperties;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 6/25/13
 * Time: 10:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Alternative
public class EclipselinkConfigurationProvider implements PersistenceConfigurationProvider {
    @Inject
    javax.sql.DataSource dataSource;

    @Override
    public Properties getEntityManagerFactoryConfiguration(String persistenceUnitName) {
        Properties p= new Properties();
        p.put(PersistenceUnitProperties.NON_JTA_DATASOURCE,dataSource);
        return p;
    }
}
