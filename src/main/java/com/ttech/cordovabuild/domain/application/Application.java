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

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ttech.cordovabuild.domain.asset.Asset;
import com.ttech.cordovabuild.domain.user.User;

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
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.REFRESH })
	private List<ApplicationBuilt> builds;
	@ManyToOne(optional = false)
	private User owner;
	@Basic
	private boolean deleted;



    @Basic
	@Column(length = 1024, nullable = true)
	private String repositoryURI;

	@OneToOne(fetch = LAZY, cascade = { CascadeType.PERSIST,
			CascadeType.REFRESH })
	private Asset sourceAsset;

	@Embedded
	private ApplicationConfig applicationConfig = new ApplicationConfig();

	public Application() {
	}

	public Application(Asset sourceAsset, User owner) {
		this.sourceAsset = sourceAsset;
		this.owner = owner;
	}

    public Application(Asset sourceAsset, ApplicationConfig applicationConfig, String repositoryURI, User owner) {
        this.sourceAsset = sourceAsset;
        this.applicationConfig = applicationConfig;
        this.repositoryURI = repositoryURI;
        this.owner = owner;
    }
	public String getRepositoryURI() {
		return repositoryURI;
	}

	public void setRepositoryURI(String sourceURI) {
		this.repositoryURI = sourceURI;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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

	public List<ApplicationBuilt> getBuilds() {
		return builds;
	}

	public void setBuilds(List<ApplicationBuilt> builds) {
		this.builds = builds;
	}

	public ApplicationConfig getApplicationConfig() {
		return applicationConfig;
	}

	public void setApplicationConfig(ApplicationConfig applicationConfig) {
		this.applicationConfig = applicationConfig;
	}

	public Asset getSourceAsset() {
		return sourceAsset;
	}

	public void setSourceAsset(Asset sourceAsset) {
		this.sourceAsset = sourceAsset;
	}
}
