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

package com.ttech.cordovabuild.domain.asset;

import com.google.common.io.ByteStreams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/6/13
 * Time: 3:07 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:hazelcastContext.xml",
        "classpath:datasourceContext.xml", "classpath:applicationContext.xml"})
public class AssetServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetServiceTest.class);
    @Autowired
    AssetService assetService;

    @Test
    public void testHandleInputStream() throws Exception {
        assertTrue(UUID.randomUUID().toString().length() <= 36);
        assertNotNull(assetService);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            builder.append(UUID.randomUUID().toString());
        }
        final byte[] data = builder.toString().getBytes();
        AssetRef assetRef = assetService.save(new ByteArrayInputStream(data), "application/octet-stream");
        assetService.handleInputStream(assetRef, new InputStreamHandler() {
            @Override
            public void handleInputStream(InputStream inputStream) throws IOException {
                byte[] bytes = ByteStreams.toByteArray(inputStream);
                boolean equals = Arrays.equals(data, bytes);
                LOGGER.info("data arrays equality is {}", equals);
                assertTrue(equals);
            }
        });
    }
}
