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


public enum BuiltType {
    ANDROID("android", "apk", Constants.ANDROID_MIME_TYPE), IOS("ios", "ipa", Constants.IOS_MIME_TYPE);

    private final String platformString;
    private final String platformSuffix;
    private final String mimeType;

    private BuiltType(String val, String platformSuffix, String mimeType) {
        this.platformString = val;
        this.platformSuffix = platformSuffix;
        this.mimeType = mimeType;
    }

    public String getPlatformString() {
        return platformString;
    }

    public String getPlatformSuffix() {
        return platformSuffix;
    }

    @Override
    public String toString() {
        return platformString;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static class Constants {
        public static final String ANDROID_MIME_TYPE = "application/vnd.android.package-archive";
        public static final String IOS_MIME_TYPE = "application/octet-stream";
    }

    public static BuiltType getValueOfIgnoreCase(String value) {
        for (BuiltType type : BuiltType.values()) {
            if (type.toString().equalsIgnoreCase(value))
                return type;
        }
        throw new IllegalArgumentException("No enum constant " + BuiltType.class.getName() + ".value");
    }
}
