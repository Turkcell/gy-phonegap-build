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

package com.ttech.cordovabuild.build.source;

import com.ttech.cordovabuild.build.git.GitRepository;

import java.io.File;

public class GitSource implements Source {
    private final String gitUri;
    private final GitRepository repository;

    public GitSource(String gitUri, GitRepository repository) {
        this.gitUri = gitUri;
        this.repository = repository;
    }

    @Override
    public void copy(File location) {
        repository.clone(gitUri,location);
    }

    @Override
    public String getURI() {
        return gitUri;
    }
}