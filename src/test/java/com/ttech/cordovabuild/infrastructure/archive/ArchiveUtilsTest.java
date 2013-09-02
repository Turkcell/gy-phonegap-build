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

package com.ttech.cordovabuild.infrastructure.archive;

import com.ttech.cordovabuild.infrastructure.git.GitUtils;
import junit.framework.Assert;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 8/20/13
 * Time: 11:55 PM
 * To change this built use File | Settings | File Templates.
 */
public class ArchiveUtilsTest {
    public static final String GIT_REPO = "https://github.com/Turkcell/RestaurantReviews.git";
    @Test
    public void createArchive() throws IOException {
        Path tempFile = Files.createTempFile(null, null);
        System.out.println(tempFile);
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tempFile.toFile()));
        ArchiveUtils.compressDirectory(GitUtils.clone(GIT_REPO), output);
        output.close();
        Assert.assertTrue(tempFile.toFile().exists());

        Path tempDirectory = Files.createTempDirectory(null);
        ArchiveUtils.extractFiles(new BufferedInputStream(new FileInputStream(tempFile.toFile())), tempDirectory);
        System.out.println(tempDirectory);
    }
}
