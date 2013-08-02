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
package com.ttech.cordovabuild.domain;

import com.ttech.cordovabuild.domain.asset.Asset;
import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author Anıl Halil
 */
@Embeddable
public class BuilTarget implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3100288908130355062L;
	private ProjectType type;
    private Asset asset;

    public ProjectType getType() {
        return type;
    }

    public void setType(ProjectType type) {
        this.type = type;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public BuilTarget() {
    }

    public BuilTarget(ProjectType type, Asset asset) {
        this.type = type;
        this.asset = asset;
    }
}
