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

import com.hazelcast.security.UsernamePasswordCredentials;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SecurityEnvironmentTest {

    private static final String ROOT_TARGET = "http://localhost:8080";
    private static Server server;

    @BeforeClass
    public static void setup() throws Exception {
        server = new Server(8080);
        WebAppContext root = new WebAppContext();
        root.setWar(Paths.get("").toAbsolutePath().toString() + "/src/main/webapp");
        root.setContextPath("/");
        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{root});
        server.setHandler(contexts);
        server.start();
    }

    @AfterClass
    public static void shutdown() throws Exception {
        server.stop();
    }

    @Test
    public void testNotAuthenticated() throws InterruptedException {
        Response response = createTarget(ROOT_TARGET).path("application").path("1")
                .request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    private WebTarget createTarget(String path) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(ROOT_TARGET);
        return target;
    }

    @Test
    @Ignore
    public void testAuthentication() {
        Response post = createTarget(ROOT_TARGET)
                .path("authenticate/login")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(new UsernamePasswordCredentials("anil",
                        "anil"), MediaType.APPLICATION_JSON_TYPE));
        assertEquals(Status.OK.getStatusCode(), post.getStatus());
    }
}
