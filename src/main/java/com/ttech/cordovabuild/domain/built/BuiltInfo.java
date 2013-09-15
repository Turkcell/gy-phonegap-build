
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

import com.ttech.cordovabuild.domain.application.BuiltTarget;
import com.ttech.cordovabuild.domain.application.BuiltType;
import com.ttech.cordovabuild.domain.asset.AssetRef;

import java.nio.file.Path;
import java.util.Date;

public class BuiltInfo {

    private final Path path;
    private final Date startDate;
    private final Long duration;
    private final BuiltType builtType;
    private final String applicationName;
    private final AssetRef assetRef;
    private final BuiltTarget.Status status;

    public BuiltInfo(Path path, Date started, Long duration, BuiltType builtType, String applicationName, AssetRef assetRef, BuiltTarget.Status status) {
        this.path = path;
        this.startDate = started;
        this.duration = duration;
        this.builtType = builtType;
        this.applicationName = applicationName;
        this.assetRef = assetRef;
        this.status = status;
    }

    public BuiltInfo(BuiltType builtType, BuiltTarget.Status status) {
        this(null, null, null, builtType, null, null, status);
    }

    public Path getPath() {
        return path;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Long getDuration() {
        return duration;
    }

    public AssetRef getAssetRef() {
        return assetRef;
    }

    public BuiltType getBuiltType() {
        return builtType;
    }

    public String getApplicationName() {
        return applicationName;
    }

    static public BuiltInfo failedFor(String applicationName, BuiltType builtType) {
        return new BuiltInfo(null, new Date(), 0L, builtType, applicationName, null, BuiltTarget.Status.FAILED);
    }

    public BuiltTarget.Status getStatus() {
        return status;
    }
}
