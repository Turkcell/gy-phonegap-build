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
package com.ttech.cordovabuild.domain.built;

import com.ttech.cordovabuild.domain.application.ApplicationBuilt;
import com.ttech.cordovabuild.domain.application.BuiltType;
import com.ttech.cordovabuild.domain.application.Application;

import com.ttech.cordovabuild.domain.application.ApplicationFeature;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ApplicationBuilderImpl implements ApplicationBuilder {

    private static Logger LOGGER = LoggerFactory
            .getLogger(ApplicationBuilderImpl.class);

    @Value("${build.path}")
    private String buildPath;

    @Value("${create.path}")
    private String createPath;

    @Override
    public BuildInfo buildApplication(Application application,
                                      ApplicationBuilt applicationBuilt, BuiltType builtType) {
        Date startDate = new Date();
        Path buildPath = createBuild(application, applicationBuilt);
        File buildPathFile = buildPath.toFile();
        addPlatformSupport(builtType, buildPathFile);
        addFeatures(application.getApplicationConfig().getFeatures(), buildPathFile);
        buildPlatform(builtType, buildPathFile);
        return new BuildInfo(buildPath, startDate, System.currentTimeMillis() - startDate.getTime(), builtType, application.getApplicationConfig().getName());
    }

    private void buildPlatform(BuiltType builtType, File buildPath) {
        try {
            runProcess(buildPath, createPath, "build", builtType.getPlatformString());
        } catch (IOException e) {
            throw new PlatformBuiltException(e);
        } catch (InterruptedException e) {
            throw new PlatformBuiltException(e);
        }
    }

    private void addFeatures(Set<ApplicationFeature> features, File buildPath) {
        LOGGER.info("adding features with size {}", features.size());
        try {
            for (ApplicationFeature applicationFeature : features) {
                addFeature(applicationFeature, buildPath);
            }
        } catch (IOException e) {
            throw new PlatformConfigurationException(e);
        } catch (InterruptedException e) {
            throw new PlatformConfigurationException(e);
        }
    }

    private void addFeature(ApplicationFeature applicationFeature, File buildPath) throws IOException, InterruptedException {
        for (String featureURI : applicationFeature.getCordovaURIs())
            runProcess(buildPath, createPath, "plugin", "add", featureURI);
    }

    private void addPlatformSupport(BuiltType builtType, File buildPath) {
        LOGGER.info("adding platform support for {}", builtType);
        try {
            int result = runProcess(buildPath, createPath, "platform", "add", builtType.getPlatformString());
            if (result != 0) {
                throw new PlatformConfigurationException(result);
            }
        } catch (IOException e) {
            throw new PlatformConfigurationException(e);
        } catch (InterruptedException e) {
            throw new PlatformConfigurationException(e);
        }
    }

    private Path createBuild(Application application, ApplicationBuilt applicationBuilt) {
        LOGGER.info("starting to create application built path for {}/{}", application.getId(), application.getOwner().getId());
        Path ownerPath = checkOwnerPath(applicationBuilt, application.getOwner().getName());
        Path templateDirectory = checkTemplateDirectory(application.getApplicationConfig().getName(), applicationBuilt, ownerPath);
        try {
            int result = runProcess(ownerPath.toFile(), createPath, "create", application.getApplicationConfig().getName(), application.getApplicationConfig().getAppPackage(), application.getApplicationConfig().getName());
            if (result == 0) {
                return templateDirectory;
            }
            throw new TemplateCreationException(result);
        } catch (IOException e) {
            throw new TemplateCreationException(e);
        } catch (InterruptedException e) {
            throw new TemplateCreationException(e);
        }
    }

    private int runProcess(File ownerPath, String... args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.directory(ownerPath);
        pb.inheritIO();
        Process p = pb.start();
        return p.waitFor();
    }

    private Path checkTemplateDirectory(String appName, ApplicationBuilt info, Path ownerPath) {
        Path templateDirectory = ownerPath.resolve(appName);
        if (Files.exists(templateDirectory)) {
            LOGGER.info("built directory {} exists",templateDirectory);
            try {
                Files.deleteIfExists(templateDirectory);
            } catch (IOException e) {
                LOGGER.error("delete operation of {} filed",templateDirectory, e);
                throw new TemplateCreationException(e);
            }
        }
        return templateDirectory;
    }

    private Path checkOwnerPath(ApplicationBuilt info, String ownerName) {

        Path ownerPath = Paths.get(buildPath, ownerName);
        if (!Files.exists(ownerPath) || !Files.isDirectory(ownerPath)) {
            LOGGER.info("built path {} cannot be found creating",
                    ownerPath);
            try {
                Files.createDirectory(ownerPath);
                LOGGER.info("built path {} created", ownerPath);
                return ownerPath;

            } catch (IOException e) {
                throw new TemplateCreationException(MessageFormat.format(
                        "owner path creation failed for {0}",
                        ownerPath.toString()));
            }
        }
        return ownerPath;
    }
}
