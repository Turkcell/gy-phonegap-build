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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:hazelcastContext.xml", "classpath:datasourceContext.xml", "classpath:applicationContext.xml"})
public class GitRepositoryTest {

    public static final String PROJECT = "base";
    @Autowired
    GitRepository repository;

    @Test
    public void testGitPurge() throws IOException {
        File tempDirectory = FileUtils.getTempDirectory();
        String project = tempDirectory + File.separator + PROJECT;
        FileUtils.forceMkdir(new File(project + File.separator + "a" + File.separator + ".git"));
        FileUtils.forceMkdir(new File(project + File.separator + "b" + File.separator + ".git"));
        Collection<File> aFiles = FileUtils.listFilesAndDirs(new File(project + File.separator + "a"), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        assertEquals(2, aFiles.size());
        Collection<File> files = FileUtils.listFilesAndDirs(new File(project), new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return false;
            }

            @Override
            public boolean accept(File dir, String name) {
                return false;
            }
        }, new IOFileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }

            @Override
            public boolean accept(File dir, String name) {
                return false;
            }
        }
                );
        assertEquals(5, files.size());
        List<File> filtered = new ArrayList<>();
        for (File file : files) {
            if (file.getName().equals(".git")) {
                filtered.add(file);
            }
        }
        assertEquals(2, filtered.size());
    }

    @Test
    public void testCloneRepository() throws IOException {
        File restaurantviewsclone = new File(FileUtils.getTempDirectoryPath(), "restaurantviewsclone");
        if (restaurantviewsclone.exists()) {
            FileUtils.deleteDirectory(restaurantviewsclone);
        }
        repository.clone("https://github.com/Turkcell/RestaurantReviews.git", restaurantviewsclone
                );
        assertTrue(restaurantviewsclone.exists());
    }
}
