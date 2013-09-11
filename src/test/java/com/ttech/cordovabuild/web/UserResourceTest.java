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
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class UserResourceTest extends BaseResourceTest {

    @Test
    public void testNotAuthenticated() throws InterruptedException {
        Response response = createTarget(ROOT_TARGET).path("application").path("1")
                .request(MediaType.APPLICATION_JSON_TYPE).get();
        assertEquals(Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void testCreateUser() {
        User user = new User("anil", "halil", "anil@anil.com", "anil", Collections.<Role>emptySet(), "password");
        Response response = createTarget(ROOT_TARGET).path("user").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(Status.OK.getStatusCode(), response.getStatusInfo().getStatusCode());
        System.out.println(response.readEntity(String.class));
    }

    @Test
    public void testLogin() {
        login(ROOT_TARGET);
    }

    @Test
    public void testHello() {
        Response response = createTarget(ROOT_TARGET).path("1").path("hello").request(MediaType.APPLICATION_JSON_TYPE).get();
        System.out.println(response.readEntity(String.class));
    }

}
