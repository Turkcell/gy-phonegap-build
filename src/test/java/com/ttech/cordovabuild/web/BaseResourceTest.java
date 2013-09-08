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

package com.ttech.cordovabuild.web;

import com.ttech.cordovabuild.domain.user.Role;
import com.ttech.cordovabuild.domain.user.User;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.glassfish.jersey.apache.connector.ApacheConnector;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.nio.file.Paths;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/8/13
 * Time: 9:52 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseResourceTest {
    private static Server server;
    private Client client;

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

    protected WebTarget createTarget(String path) {
        if (client == null)
            client = getClient();
        WebTarget target = client.target(path);
        return target;
    }

    protected Client getClient() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.connector(new ApacheConnector(null));
        return ClientBuilder.newBuilder().withConfig(clientConfig).build();
    }

    protected void login(String path) {
        User user = new User("anil", "halil", "achalil@gmail.com", "capacman", Collections.<Role>emptySet(), "password");
        Response userResponse = createTarget(path).path("user").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(Response.Status.OK.getStatusCode(), userResponse.getStatusInfo().getStatusCode());
        userResponse.readEntity(String.class);
        Response loginResponse = createTarget(path).path("login").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(Response.Status.OK.getStatusCode(), loginResponse.getStatusInfo().getStatusCode());
        loginResponse.readEntity(String.class);
    }
}
