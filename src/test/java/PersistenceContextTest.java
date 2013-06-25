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

import com.ttech.cordovabuild.asset.Asset;
import com.ttech.cordovabuild.asset.AssetRepository;
import com.ttech.cordovabuild.persistence.DataSource;
import org.apache.deltaspike.jpa.api.transaction.TransactionScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.util.Properties;

import static junit.framework.Assert.assertNotNull;


@RunWith(Arquillian.class)
public class PersistenceContextTest {
    @Inject
    @DataSource
    Properties properties;

    @Inject
    AssetRepository assetRepository;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive jar = ShrinkWrap.create(WebArchive.class, "ROOT.war")
                .addPackages(true, "com.ttech.cordovabuild.persistence","com.ttech.cordovabuild.asset")
                .addAsWebResource("cordova.properties")
                .addAsWebResource("dataSource.properties")
                .addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
        System.out.println(jar.toString(true));
        return jar;
    }

    @Test
    public void testDataSourceConfig() {
        assertNotNull(properties);
        Object driverClassName = properties.get("driverClassName");
        assertNotNull(driverClassName);
        System.out.println(driverClassName);
    }

    @Test
    public void testAssetRepository() {
        assertNotNull(assetRepository);
        Asset a=new Asset();
        a.setData("test".getBytes());
        assetRepository.save(a);
        assertNotNull(a.getId());
    }
}
