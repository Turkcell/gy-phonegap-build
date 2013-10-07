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
package com.ttech.cordovabuild.domain.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ttech.cordovabuild.domain.asset.AssetRef;
import com.ttech.cordovabuild.domain.built.BuiltInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Anıl Halil
 */
@Embeddable
public class BuiltTarget implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3100288908130355062L;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Basic
    @Column(nullable = true)
    private Long duration;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date startDate;
    @Enumerated(EnumType.STRING)
    private BuiltType type;
    @JsonIgnore
    @Embedded
    private AssetRef assetRef;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Transient
    @JsonIgnore
    private ApplicationBuilt applicationBuilt;


    public BuiltType getType() {
        return type;
    }

    public void setType(BuiltType type) {
        this.type = type;
    }

    public AssetRef getAssetRef() {
        return assetRef;
    }

    public void setAssetRef(AssetRef assetRef) {
        this.assetRef = assetRef;
    }

    public BuiltTarget() {
    }

    public BuiltTarget(BuiltType builtType) {
        this.type = builtType;
        this.status = Status.WAITING;
    }

    public BuiltTarget(BuiltInfo builtInfo) {
        this.type = builtInfo.getBuiltType();
        this.assetRef = builtInfo.getAssetRef();
        this.startDate = builtInfo.getStartDate();
        this.duration = builtInfo.getDuration();
        this.status = builtInfo.getStatus();
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    public String getUrl() {
        if (status == Status.SUCCESS)
            return "/application/" + this.applicationBuilt.getApplication().getId() + "/download/" + getType().toString();
        return null;
    }

    public void setApplicationBuilt(ApplicationBuilt applicationBuilt) {
        this.applicationBuilt = applicationBuilt;
    }

    public static enum Status {
        FAILED, SUCCESS, STARTED, WAITING
    }
}
