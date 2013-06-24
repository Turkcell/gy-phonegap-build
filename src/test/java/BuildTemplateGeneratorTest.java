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

import com.ttech.cordovabuild.BuildInfo;
import com.ttech.cordovabuild.Owner;
import com.ttech.cordovabuild.build.template.BuildTemplate;
import com.ttech.cordovabuild.build.template.BuildTemplateGenerator;
import org.apache.deltaspike.core.api.config.ConfigProperty;
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
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(Arquillian.class)
public class BuildTemplateGeneratorTest {
    @Inject
    @ConfigProperty(name="build.path")
    private String buildPath;
    @Inject
    BuildTemplateGenerator generator;

    @Deployment
    public static WebArchive createDeployment() {
        WebArchive jar = ShrinkWrap.create(WebArchive.class, "ROOT.war")
                .addPackage("com.ttech.cordovabuild.build")
                .addAsWebResource("cordova.properties")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
        System.out.println(jar.toString(true));
        return jar;
    }

    @Test
    public void checkBuildPath() throws IOException, InterruptedException {
        assertNotNull(buildPath);
    }

    @Test
    public void testTemplateCreation(){
        BuildInfo buildInfo = new BuildInfo(null, new Owner("anil"), "deneme", null);
        BuildTemplate template = generator.generateTemplate(buildInfo);
        File path = new File(new File(buildPath, buildInfo.getOwner().getName()), buildInfo.getName());
        assertEquals(path.getAbsolutePath(),template.getPath());
    }
}