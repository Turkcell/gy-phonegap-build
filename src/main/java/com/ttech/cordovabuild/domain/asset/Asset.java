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

package com.ttech.cordovabuild.domain.asset;


import javax.persistence.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;


@Entity
public class Asset {
    @Id
    @GeneratedValue
    private Long id;
    @Lob
    @Column(length = 10 * 1024 * 1024)
    private byte[] data;


    public Asset() {
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Asset(long id) {
        this.id = id;
    }

    public InputStream asInputStream() {
        return new ByteArrayInputStream(data);
    }

    public String toString(){
        return "assetRef"+id;
    }

}
