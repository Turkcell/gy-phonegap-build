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

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.ttech.cordovabuild.domain.application.ApplicationBuilt;
import com.ttech.cordovabuild.domain.application.BuiltTarget;
import com.ttech.cordovabuild.domain.application.BuiltType;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        ApplicationBuilt app = createAppResponse.readEntity(ApplicationBuilt.class);
        assertEquals(Response.Status.OK.getStatusCode(), createAppResponse.getStatus());
        Response findResponse = createTarget(ROOT_TARGET).path("application").path(app.getId().toString()).request(MediaType.APPLICATION_JSON_TYPE).get();
        ApplicationBuilt findApp = findResponse.readEntity(ApplicationBuilt.class);
        assertEquals(Response.Status.OK.getStatusCode(), findResponse.getStatus());
        assertEquals(app.getApplication().getId(), findApp.getApplication().getId());
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

    @Test
    public void testQrImage() throws IOException, FormatException, ChecksumException, NotFoundException {
        login(ROOT_TARGET);
        Response createAppResponse = createTarget(ROOT_TARGET).path("application").queryParam("sourceUri", "https://github.com/Turkcell/RestaurantReviews.git").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(null, MediaType.APPLICATION_JSON_TYPE));
        ApplicationBuilt app = createAppResponse.readEntity(ApplicationBuilt.class);
        WebTarget path = createTarget(ROOT_TARGET).path("application").path(app.getApplication().getId().toString()).path("qrimage");
        Response response = path.request("image/jpeg").get();
        String prefix = path.getUri().resolve(".").resolve("install").normalize().toString() + "?qrKey=";
        QRCodeReader qrCodeReader = new QRCodeReader();
        Result result = qrCodeReader.decode(new BinaryBitmap(new GlobalHistogramBinarizer(new BufferedImageLuminanceSource(ImageIO.read(response.readEntity(InputStream.class))))));
        assertTrue(result.getText().startsWith(prefix));
    }

    @Test
    public void testApk() throws InterruptedException {
        login(ROOT_TARGET);
        Response createAppResponse = createTarget(ROOT_TARGET).path("application").queryParam("sourceUri", "https://github.com/Turkcell/RestaurantReviews.git").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(null, MediaType.APPLICATION_JSON_TYPE));
        ApplicationBuilt app = createAppResponse.readEntity(ApplicationBuilt.class);
        ApplicationBuilt applicationBuilt = pollBuilt(app);
    }

    private ApplicationBuilt pollBuilt(ApplicationBuilt app) throws InterruptedException {

        while (true) {
            Response response = createTarget(ROOT_TARGET).path("application").path(String.valueOf(app.getApplication().getId())).request(MediaType.APPLICATION_JSON_TYPE).get();
            ApplicationBuilt applicationBuilt = response.readEntity(ApplicationBuilt.class);
            for (BuiltTarget builtTarget : applicationBuilt.getBuiltTargets()) {
                if (!builtTarget.getType().equals(BuiltType.ANDROID))
                    continue;
                assertFalse(builtTarget.getStatus().equals(BuiltTarget.Status.FAILED));
                if (builtTarget.getStatus().equals(BuiltTarget.Status.SUCCESS))
                    return applicationBuilt;
            }
            Thread.sleep(200);
        }
    }

}
