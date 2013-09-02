
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

import com.google.common.io.Files;
import com.ttech.cordovabuild.domain.application.BuiltType;
import com.ttech.cordovabuild.domain.asset.Asset;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

public class BuildInfo {

    private final Path path;
    private final Date startDate;
    private final long duration;
    private final BuiltType builtType;
    private final String applicationName;
    private Asset asset;

    public BuildInfo(Path path, Date started, long duration, BuiltType builtType, String applicationName) {
        this.path = path;
        this.startDate = started;
        this.duration = duration;
        this.builtType = builtType;
        this.applicationName = applicationName;
    }

    public Path getPath() {
        return path;
    }

    public Date getStartDate() {
        return startDate;
    }

    public long getDuration() {
        return duration;
    }

    public Asset getAsset() {
        Path assetPath = path.resolve("platforms").resolve(builtType.getPlatformString()).resolve("bin").resolve(applicationName.concat(".apk"));
        try {
            return new Asset(Files.toByteArray(assetPath.toFile()));
        } catch (FileNotFoundException e) {
            throw new PlatformBuiltException(e);
        } catch (IOException e) {
            throw new PlatformBuiltException(e);
        }
    }

    public BuiltType getBuiltType() {
        return builtType;
    }

    public String getApplicationName() {
        return applicationName;
    }
}
