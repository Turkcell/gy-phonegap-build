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

package com.ttech.cordovabuild.infrastructure;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.hibernate.engine.jdbc.internal.Formatter;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.persistence.EntityManagerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/17/13
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class DDLExport {
    @Test
    public void exportDDL() {


        Formatter formatter = FormatStyle.DDL.getFormatter();

        Ejb3Configuration jpaConfiguration = new Ejb3Configuration().configure("cordova", null);
        Configuration hibernateConfiguration = jpaConfiguration.getHibernateConfiguration();
        SchemaExport export = new SchemaExport(hibernateConfiguration);
        export.setOutputFile("my-schema.sql");
        export.setDelimiter(";");
        export.execute(true, false, false, true);
    }
}
