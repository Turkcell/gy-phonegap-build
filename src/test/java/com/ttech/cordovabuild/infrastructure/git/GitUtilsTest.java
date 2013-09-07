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
package com.ttech.cordovabuild.infrastructure.git;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GitUtilsTest {

    public static final String PROJECT = "base";

    @Test
    public void testGitPurge() throws IOException {
        Path restaurantviewsclone = GitUtils.clone("https://github.com/Turkcell/RestaurantReviews.git");
        Files.walkFileTree(restaurantviewsclone,new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                assertFalse(dir.endsWith(".git"));
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @Test
    public void testCloneRepository() throws IOException {
        Path restaurantviewsclone = GitUtils.clone("https://github.com/Turkcell/RestaurantReviews.git");
        assertTrue(restaurantviewsclone.toFile().exists());
    }
}
