/*
 * Copyright 2013 Turkcell Teknoloji Inc. and individual
 * contributors by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ttech.cordovabuild.domain.application;

import com.ttech.cordovabuild.domain.application.source.ApplicationSource;
import com.ttech.cordovabuild.domain.application.source.ApplicationSourceFactory;
import com.ttech.cordovabuild.domain.asset.Asset;
import com.ttech.cordovabuild.domain.user.User;
import com.ttech.cordovabuild.infrastructure.git.GitUtils;
import com.ttech.cordovabuild.infrastructure.queue.BuiltQueuePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;

/**
 * @author Anıl Halil
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ApplicationServiceImpl.class);
    @Autowired
    ApplicationRepository repository;
    @Autowired
    ApplicationSourceFactory sourceFactory;
    @Autowired
    BuiltQueuePublisher builtQueuePublisher;

    @Override
    public Application createApplication(User owner, String repositoryURI) {
        LOGGER.info(
                "new application will be created for {} with repository {}",
                owner, repositoryURI);
        Path localPath = GitUtils.clone(repositoryURI);
        LOGGER.info("clone finished");
        ApplicationSource applicationSource = sourceFactory.createSource(localPath);
        return repository.saveApplication(new Application(applicationSource.toAsset(), applicationSource.getApplicationConfig(), repositoryURI, owner));
    }

    @Override
    public Application createApplication(User owner, Asset asset) {
        ApplicationSource source = sourceFactory.createSource(asset);
        return repository.saveApplication(new Application(asset, source.getApplicationConfig(), null, owner));
    }

    @Override
    public ApplicationBuilt buildApplication(Application application) {
        ApplicationBuilt built = new ApplicationBuilt(application);
        application.getBuilds().add(built);
        repository.saveApplication(application);
        ApplicationBuilt applicationBuilt = application.getBuilds().get(0);
        builtQueuePublisher.publishBuilt(applicationBuilt);
        return applicationBuilt;
    }

    @Override
    public Application findApplication(Long id) {
        return repository.findById(id);
    }

    @Override
    public ApplicationBuilt findApplicationBuilt(Long id) {
        return repository.findApplicationBuild(id);
    }

    @Override
    public Application findByApplicationBuilt(ApplicationBuilt applicationBuilt) {
        return repository.findByApplicationBuild(applicationBuilt);
    }

    @Override
    public ApplicationBuilt addBuiltTarget(ApplicationBuilt applicationBuilt, BuiltTarget builtTarget) {
        return repository.addBuiltTarget(applicationBuilt, builtTarget);
    }

}
