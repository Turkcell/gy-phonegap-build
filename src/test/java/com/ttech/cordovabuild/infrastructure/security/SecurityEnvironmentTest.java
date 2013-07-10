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

package com.ttech.cordovabuild.infrastructure.security;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.hazelcast.security.UsernamePasswordCredentials;
import com.ttech.cordovabuild.infrastructure.CustomPropertyFileConfig;
import com.ttech.cordovabuild.web.Example;

@RunWith(Arquillian.class)
public class SecurityEnvironmentTest {

	private static final String ROOT_TARGET = "http://localhost:9091/ROOT/resources";

	@Deployment
	public static WebArchive createDeployment() {
		WebArchive jar = ShrinkWrap
				.create(WebArchive.class, "ROOT.war")
				.addPackages(true, ShiroConfig.class.getPackage(),
						Example.class.getPackage())
				.addClass(CustomPropertyFileConfig.class)
				.addAsWebResource("cordova.properties")
				.addAsWebResource("logback.xml")
				.addAsWebResource("users.txt")

				.addAsWebInfResource(EmptyAsset.INSTANCE,
						ArchivePaths.create("beans.xml"))
				.setWebXML(new File("src/main/webapp/WEB-INF/web.xml"));
		System.out.println(jar.toString(true));
		return jar;
	}

	@Test
	public void testNotAuthenticated() throws InterruptedException {
		Response response = createTarget(ROOT_TARGET).path("example")
				.request(MediaType.APPLICATION_JSON_TYPE).get();
		assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
	}

	private WebTarget createTarget(String path) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(ROOT_TARGET);
		return target;
	}

	@Test
	public void testAuthentication() {
		Response post = createTarget(ROOT_TARGET)
				.path("authenticate/login")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.post(Entity.entity(new UsernamePasswordCredentials("anil",
						"anil"), MediaType.APPLICATION_JSON_TYPE));
		assertEquals(Status.OK.getStatusCode(), post.getStatus());
	}
}
