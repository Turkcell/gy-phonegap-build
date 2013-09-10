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
import com.ttech.cordovabuild.domain.asset.AssetRef;
import com.ttech.cordovabuild.domain.user.User;
import com.ttech.cordovabuild.domain.user.UserRepository;
import com.ttech.cordovabuild.infrastructure.git.GitUtils;
import com.ttech.cordovabuild.infrastructure.queue.BuiltQueuePublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.text.MessageFormat;

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

    @Autowired
    UserRepository userRepository;

    @Override
    public Application createApplication(User owner, String repositoryURI) {
        LOGGER.info(
                "new application will be created for {} with repository {}",
                owner, repositoryURI);
        ApplicationSource applicationSource = createApplicationSource(repositoryURI);
        return repository.saveApplication(new Application(applicationSource.toAsset(), applicationSource.getApplicationConfig(), repositoryURI, owner));
    }

    private ApplicationSource createApplicationSource(String repositoryURI) {
        Path localPath = GitUtils.clone(repositoryURI);
        LOGGER.info("clone finished");
        return sourceFactory.createSource(localPath);
    }

    @Override
    public Application createApplication(String userName, String repositoryURI) {
        return createApplication(userRepository.findUserByUserName(userName), repositoryURI);
    }

    @Override
    public Application createApplication(User owner, AssetRef assetRef) {
        ApplicationSource source = sourceFactory.createSource(assetRef);
        return repository.saveApplication(new Application(assetRef, source.getApplicationConfig(), null, owner));
    }

    @Override
    public Application updateApplicationCode(Long id) {
        Application application = findApplication(id);
        if (!application.hasRepositoryUri())
            throw new ApplicationUpdateException(MessageFormat.format("application with id {0} does not have any repositoryURI", application.getId()));
        return updateApplicationSourceInner(application, createApplicationSource(application.getRepositoryURI()).toAsset());
    }

    private Application updateApplicationSourceInner(Application application, AssetRef assetRef) {
        application.setSourceAssetRef(assetRef);
        return repository.updateApplication(application);
    }

    @Override
    public Application updateApplicationCode(Long id, AssetRef assetRef) {
        Application application = findApplication(id);
        if (application.hasRepositoryUri())
            throw new ApplicationUpdateException(MessageFormat.format("application with id {0} does have repositoryURI {1}", application.getId(), application.getRepositoryURI()));
        return updateApplicationSourceInner(application, assetRef);
    }

    @Override
    public ApplicationBuilt prepareApplicationBuilt(Application application) {
        return repository.saveApplicationBuilt(new ApplicationBuilt(application));
    }

    @Override
    public ApplicationBuilt prepareApplicationBuilt(Long id) {
        return prepareApplicationBuilt(findApplication(id));
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
    public ApplicationBuilt updateApplicationBuilt(ApplicationBuilt applicationBuilt) {
        return repository.updateApplicationBuilt(applicationBuilt);
    }

    @Override
    public ApplicationBuilt getLatestBuilt(Long id) {
        return getLatestBuilt(findApplication(id));
    }

    @Override
    public ApplicationBuilt getLatestBuilt(Application application) {
        return repository.getLatestBuilt(application);
    }

}
