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
import com.ttech.cordovabuild.domain.asset.AssetRef;
import com.ttech.cordovabuild.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
public class Application implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -735262415872708429L;
    @SequenceGenerator(allocationSize = 50, name = "APP_SEQ")
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "APP_SEQ")
    private Long id;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @JsonIgnore
    @ManyToOne(optional = false)
    private User owner;

    @Basic
    @Column(length = 1024, nullable = true)
    private String repositoryURI;

    @JsonIgnore
    @Embedded
    private AssetRef sourceAssetRef;

    @Embedded
    private ApplicationConfig applicationConfig = new ApplicationConfig();

    public Application() {
    }

    public Application(AssetRef sourceAssetRef, ApplicationConfig applicationConfig, String repositoryURI, User owner) {
        this.sourceAssetRef = sourceAssetRef;
        this.applicationConfig = applicationConfig;
        this.repositoryURI = repositoryURI;
        this.owner = owner;
        this.created = new Date();
    }

    public String getRepositoryURI() {
        return repositoryURI;
    }

    public void setRepositoryURI(String sourceURI) {
        this.repositoryURI = sourceURI;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    public void setApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public AssetRef getSourceAssetRef() {
        return sourceAssetRef;
    }

    public void setSourceAssetRef(AssetRef sourceAssetRef) {
        this.sourceAssetRef = sourceAssetRef;
    }
}
