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

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 8/23/13
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ApplicationFeature {
    ACCELEROMETER(new String[]{"org.apache.cordova.core.device-motion"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-device-motion.git"}),
    CAMERA(new String[]{"org.apache.cordova.core.camera"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-camera.git"}),
    CAPTURE(new String[]{"org.apache.cordova.core.media-capture"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-media-capture.git"}),
    COMPASS(new String[]{"org.apache.cordova.core.device-orientation"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-device-orientation.git"}),
    CONNECTION(new String[]{"org.apache.cordova.core.network-information"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-network-information.git"}),
    CONTACTS(new String[]{"org.apache.cordova.core.contacts"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-contacts.git"}),
    DEVICE(new String[]{"org.apache.cordova.core.device"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-device.git"}),
    EVENTS(new String[]{"org.apache.cordova.core.battery-status"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-battery-status.git"}),
    FILE(new String[]{"org.apache.cordova.core.file", "org.apache.cordova.core.file-transfer"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-file.git", "https://git-wip-us.apache.org/repos/asf/cordova-plugin-file-transfer.git"}),
    GEOLOCATION(new String[]{"org.apache.cordova.core.geolocation"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-geolocation.git"}),
    GLOBALIZATION(new String[]{"org.apache.cordova.core.globalization"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-globalization.git"}),
    INAPPBROWSER(new String[]{"org.apache.cordova.core.inappbrowser"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-inappbrowser.git"}),
    MEDIA(new String[]{}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-media.git"}),
    NOTIFICATION(new String[]{"org.apache.cordova.core.dialogs", "org.apache.cordova.core.vibration"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-vibration.git", "https://git-wip-us.apache.org/repos/asf/cordova-plugin-dialogs.git"}),
    SPLASHSCREEN(new String[]{"org.apache.cordova.core.splashscreen"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-splashscreen.git"});

    private final String[] cordovaPlugins;
    private final String[] cordovaURIs;

    private ApplicationFeature(String[] cordovaPlugins, String[] cordovaURIs) {
        this.cordovaPlugins = cordovaPlugins;
        this.cordovaURIs = cordovaURIs;
    }

    private String[] getCordovaPlugins() {
        return cordovaPlugins;
    }

    private String[] getCordovaURIs() {
        return cordovaURIs;
    }
}
