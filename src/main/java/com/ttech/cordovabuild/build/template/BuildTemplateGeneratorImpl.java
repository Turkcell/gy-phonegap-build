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

package com.ttech.cordovabuild.build.template;

import com.ttech.cordovabuild.BuildInfo;
import com.ttech.cordovabuild.ProjectType;
import org.apache.commons.io.FileUtils;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;


@ApplicationScoped
public class BuildTemplateGeneratorImpl implements BuildTemplateGenerator {
    private static Logger LOGGER= LoggerFactory.getLogger(BuildTemplateGeneratorImpl.class);
    @Inject
    @ConfigProperty(name="build.path")
    private String buildPath;

    @Override
    public BuildTemplate generateTemplate(BuildInfo info) {
        File ownerPath = checkOwnerPath(info);
        ProcessBuilder pb=new ProcessBuilder("create",info.getName());
        File templateDirectory = checkTemplateDirectory(info, ownerPath);
        pb.directory(ownerPath);
        try {
            Process p = pb.start();
            int result = p.waitFor();
            if(result==0)
                return new BuildTemplate(ProjectType.ANDROID,templateDirectory.getAbsolutePath());
            throw new TemplateCreationException(result);
        } catch (IOException e) {
           throw new TemplateCreationException(e);
        } catch (InterruptedException e) {
            throw new TemplateCreationException(e);
        }

    }

    private File checkTemplateDirectory(BuildInfo info, File ownerPath) {
        File templateDirectory = new File(ownerPath, info.getName());
        if(templateDirectory.exists()){
            LOGGER.info("template directory {} exists",templateDirectory.getAbsolutePath());
            try {
                FileUtils.deleteDirectory(templateDirectory);
            } catch (IOException e) {
                LOGGER.error("delete operation of {} filed",templateDirectory.getAbsolutePath(),e);
                throw new TemplateCreationException(e);
            }
        }
        return templateDirectory;
    }

    private File checkOwnerPath(BuildInfo info) {
        File ownerPath = new File(buildPath, info.getOwner().getName());
        if(!ownerPath.exists() || !ownerPath.isDirectory()){
            LOGGER.info("build path {} cannot be found creating",ownerPath.getAbsolutePath());
            if(ownerPath.mkdir())
                LOGGER.info("build path {} created",ownerPath.getAbsolutePath());
            else
                throw new TemplateCreationException(MessageFormat.format("owner path creation failed for {0}",ownerPath.getAbsolutePath()));
        }
        return ownerPath;
    }
}
