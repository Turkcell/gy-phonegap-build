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

import com.ttech.cordovabuild.domain.asset.Asset;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
public class ApplicationBuilt implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7425285261159480420L;
    @SequenceGenerator(allocationSize = 50, name = "APP_BUILT_SEQ")
    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "APP_BUILT_SEQ")
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Embedded
    private ApplicationConfig builtConfig;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Asset builtAsset;
    @ElementCollection
    private List<BuiltTarget> builtTargets = new ArrayList<>();

    @Version
    private int version;

    public ApplicationBuilt() {
        this.startDate = new Date();
    }

    public ApplicationBuilt(Application application) {
        this();
        this.builtAsset = application.getSourceAsset();
        this.builtConfig = new ApplicationConfig(application.getApplicationConfig());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<BuiltTarget> getBuiltTargets() {
        return builtTargets;
    }

    public void setBuiltTargets(List<BuiltTarget> builtTargets) {
        this.builtTargets = builtTargets;
    }

    public ApplicationConfig getBuiltConfig() {
        return builtConfig;
    }

    public void setBuiltConfig(ApplicationConfig builtConfig) {
        this.builtConfig = builtConfig;
    }

    public Asset getBuiltAsset() {
        return builtAsset;
    }

    public void setBuiltAsset(Asset builtAsset) {
        this.builtAsset = builtAsset;
    }
}
