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

import com.ttech.cordovabuild.domain.application.Application;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/8/13
 * Time: 9:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationResourceTest extends BaseResourceTest {
    @Test
    public void testGetApplication() throws Exception {
        login(ROOT_TARGET);
        Response createAppResponse = createTarget(ROOT_TARGET).path("application").queryParam("sourceUri", "https://github.com/Turkcell/RestaurantReviews.git").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(null, MediaType.APPLICATION_JSON_TYPE));
        Application app = createAppResponse.readEntity(Application.class);
        assertEquals(Response.Status.OK.getStatusCode(), createAppResponse.getStatus());
        Response findResponse = createTarget(ROOT_TARGET).path("application").path(app.getId().toString()).request(MediaType.APPLICATION_JSON_TYPE).get();
        Application findApp = findResponse.readEntity(Application.class);
        assertEquals(app.getId(), findApp.getId());
    }

    @Test
    public void testCreateApplication() throws Exception {
        login(ROOT_TARGET);
        Response createAppResponse = createTarget(ROOT_TARGET).path("application").queryParam("sourceUri", "https://github.com/Turkcell/RestaurantReviews.git").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(null, MediaType.APPLICATION_JSON_TYPE));
        createAppResponse.readEntity(String.class);
        assertEquals(Response.Status.OK.getStatusCode(), createAppResponse.getStatus());
    }

    @Test
    public void testNotFoundApplication() {
        login(ROOT_TARGET);
        Response application = createTarget(ROOT_TARGET).path("application").path("-1").request(MediaType.APPLICATION_JSON_TYPE).get();
        application.readEntity(String.class);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), application.getStatus());
    }

}
