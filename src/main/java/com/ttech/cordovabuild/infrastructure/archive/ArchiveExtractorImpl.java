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

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class ArchiveExtractorImpl implements ArchiveExtractor {

    ArchiveStreamFactory factory=new ArchiveStreamFactory();

    @Override
    public void extractArchive(InputStream is, File location) {
        try(ArchiveInputStream ais = factory.createArchiveInputStream(is);) {
            ArchiveEntry ae;
            while((ae=ais.getNextEntry()) != null){
                if(ae.isDirectory())
                    continue;
                try(FileOutputStream outputStream = FileUtils.openOutputStream(new File(location, ae.getName()))){
                    IOUtils.copy(ais,outputStream);
                }
            }
        } catch (ArchiveException e) {
            throw new ArchiveExtractionException(e);
        } catch (IOException e) {
            throw new ArchiveExtractionException(e);
        }
    }
}
