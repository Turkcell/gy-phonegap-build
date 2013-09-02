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
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Objects;

public class GitUtils {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(GitUtils.class);


    public static Path clone(String uri) {
        Path path = null;
        try {
            path = getClonePath();
        } catch (IOException e) {
            throw new GitException(e);
        }
        try {
            Git.cloneRepository().setURI(uri).setDirectory(path.toFile()).call();
            removePattern(path, ".git");
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
        return path;
    }

    private static Path getClonePath() throws IOException {
        return Files.createTempDirectory(null);
    }

    private static void removePattern(Path path, final String s) {

        try {
            Files.walkFileTree(path, new FileVisitor<Path>() {
                private Path git = null;

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    if (dir.endsWith(s) && git == null) {
                        git = dir;
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (git != null) {
                        Files.delete(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    if (git != null) {
                        Files.deleteIfExists(file);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc == null) {
                        if (git != null) {
                            Files.delete(dir);
                            if(git.equals(dir))
                                git = null;
                        }
                        return FileVisitResult.CONTINUE;
                    }
                    throw exc;
                }
            });
        } catch (IOException e) {
            LOGGER.warn("git director purge failed", e);
        }

    }


    public static Path clone(String uri, String username, String password) {
        Path path = null;
        try {
            path = getClonePath();
        } catch (IOException e) {
            throw new GitException(e);
        }
        LOGGER.info("starting to clone git repo {} to {}", uri, path);
        try {
            Git.cloneRepository()
                    .setURI(uri)
                    .setDirectory(path.toFile())
                    .setCredentialsProvider(
                            new UsernamePasswordCredentialsProvider(username,
                                    password)).call();
            LOGGER.info("purging .git files");
            removePattern(path, ".git");
            LOGGER.info("purging .git files finished");
        } catch (GitAPIException e) {
            throw new GitException(e);
        }
        return path;
    }
}
