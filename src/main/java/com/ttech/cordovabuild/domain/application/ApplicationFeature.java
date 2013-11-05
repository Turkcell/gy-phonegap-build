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
 * To change this built use File | Settings | File Templates.
 */
public enum ApplicationFeature {
    ACCELEROMETER(new String[]{"org.apache.cordova.device-motion"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-device-motion.git"}),
    CAMERA(new String[]{"org.apache.cordova.camera"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-camera.git"}),
    CAPTURE(new String[]{"org.apache.cordova.media-capture"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-media-capture.git"}),
    COMPASS(new String[]{"org.apache.cordova.device-orientation"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-device-orientation.git"}),
    CONNECTION(new String[]{"org.apache.cordova.network-information"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-network-information.git"}),
    CONTACTS(new String[]{"org.apache.cordova.contacts"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-contacts.git"}),
    DEVICE(new String[]{"org.apache.cordova.device"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-device.git"}),
    EVENTS(new String[]{"org.apache.cordova.battery-status"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-battery-status.git"}),
    FILE(new String[]{"org.apache.cordova.file", "org.apache.cordova.core.file-transfer"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-file.git", "https://git-wip-us.apache.org/repos/asf/cordova-plugin-file-transfer.git"}),
    GEOLOCATION(new String[]{"org.apache.cordova.geolocation"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-geolocation.git"}),
    GLOBALIZATION(new String[]{"org.apache.cordova.globalization"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-globalization.git"}),
    INAPPBROWSER(new String[]{"org.apache.cordova.inappbrowser"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-inappbrowser.git"}),
    MEDIA(new String[]{"org.apache.cordova.media"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-media.git"}),
    NOTIFICATION(new String[]{"org.apache.cordova.dialogs", "org.apache.cordova.vibration"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-vibration.git", "https://git-wip-us.apache.org/repos/asf/cordova-plugin-dialogs.git"}),
    SPLASHSCREEN(new String[]{"org.apache.cordova.splashscreen"}, new String[]{"https://git-wip-us.apache.org/repos/asf/cordova-plugin-splashscreen.git"});

    private final String[] cordovaPlugins;
    private final String[] cordovaURIs;

    private ApplicationFeature(String[] cordovaPlugins, String[] cordovaURIs) {
        this.cordovaPlugins = cordovaPlugins;
        this.cordovaURIs = cordovaURIs;
    }

    public String[] getCordovaPlugins() {
        return cordovaPlugins;
    }

    public String[] getCordovaURIs() {
        return cordovaURIs;
    }

    public static ApplicationFeature fromValue(String featureValue) {
        return ApplicationFeature.valueOf(featureValue.toUpperCase());
    }
}
