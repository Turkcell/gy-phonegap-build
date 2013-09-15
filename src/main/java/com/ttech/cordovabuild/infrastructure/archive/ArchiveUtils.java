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

import com.ttech.cordovabuild.domain.application.source.ApplicationSourceException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;

public class ArchiveUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArchiveUtils.class);

    public static void extractFiles(InputStream is, Path localPath) {
        ArchiveStreamFactory archiveStreamFactory = new ArchiveStreamFactory();
        try {
            Files.createDirectories(localPath);
        } catch (IOException e) {
            throw new ArchiveExtractionException(e);
        }
        try (ArchiveInputStream ais = archiveStreamFactory.createArchiveInputStream(is);) {
            extractArchive(localPath, ais);
        } catch (ArchiveException e) {
            LOGGER.info("archiveFactory could not determine archive file type probably tar.gz");
            try (ArchiveInputStream ais = new TarArchiveInputStream(new GzipCompressorInputStream(is))) {
                extractArchive(localPath, ais);
            } catch (IOException e1) {
                throw new ArchiveExtractionException(e1);
            }
        } catch (IOException e) {
            throw new ArchiveExtractionException(e);
        }


    }

    private static void extractArchive(Path localPath, ArchiveInputStream ais) throws IOException {
        ArchiveEntry ae;
        while ((ae = ais.getNextEntry()) != null) {
            if (ae.isDirectory()) {
                continue;
            }
            Path filePath = localPath.resolve(ae.getName());
            if (!filePath.getParent().equals(localPath))
                Files.createDirectories(filePath.getParent());
            try (OutputStream outputStream = Files.newOutputStream(filePath)) {
                IOUtils.copy(ais, outputStream);
            }
        }
    }

    public static void compressDirectory(Path path, OutputStream output) {
        try {
            // Wrap the output file stream in streams that will tar and gzip everything
            TarArchiveOutputStream taos = new TarArchiveOutputStream(
                    new GZIPOutputStream(output));
            // TAR has an 8 gig file limit by default, this gets around that
            taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR); // to get past the 8 gig limit
            // TAR originally didn't support long file names, so enable the support for it
            taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            for (File child : path.toFile().listFiles()) {
                addFileToTarGz(taos, child.toPath(), "");
            }
            taos.close();
        } catch (IOException e) {
            throw new ApplicationSourceException(e);
        }
    }

    private static void addFileToTarGz(TarArchiveOutputStream tOut, Path path, String base) throws IOException {
        File f = path.toFile();
        String entryName = base + f.getName();
        TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
        tOut.putArchiveEntry(tarEntry);
        if (f.isFile()) {
            IOUtils.copy(new FileInputStream(f), tOut);
            tOut.closeArchiveEntry();
        } else {
            tOut.closeArchiveEntry();
            File[] children = f.listFiles();
            if (children != null) {
                for (File child : children) {
                    addFileToTarGz(tOut, child.toPath().toAbsolutePath(), entryName + "/");
                }
            }
        }
    }
}