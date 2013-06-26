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

import org.apache.commons.dbcp.BasicDataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Properties;


@ApplicationScoped
public class DataSourceConfig implements org.apache.deltaspike.jpa.api.datasource.DataSourceConfig {
    private static final String CONNECTION_DRIVER = "driverClassName";
    @Inject
    @DataSource
    Properties properties;

    @Override
    public String getJndiResourceName(String connectionId) {
        return null;
    }

    @Override
    public String getConnectionClassName(String connectionId) {
        return BasicDataSource.class.getName();
    }

    @Override
    public Properties getConnectionProperties(String connectionId) {
        return properties;
    }

    @Override
    public String getJdbcConnectionUrl(String connectionId) {
        return null;
    }
}
