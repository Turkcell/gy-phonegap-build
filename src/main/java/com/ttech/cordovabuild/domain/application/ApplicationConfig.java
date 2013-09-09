package com.ttech.cordovabuild.domain.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Embeddable
public class ApplicationConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);

    @Column(length = 1024)
    @Basic
    private String applicationPackage;
    @Column(length = 200)
    @Basic
    private String applicationVersion;
    @Column(length = 200)
    @Basic
    private String phoneGapversion;
    @Column(length = 1024)
    @Basic
    private String applicationName;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<ApplicationFeature> features;

    public ApplicationConfig(String applicationName, String applicationPackage, String applicationVersion,
                             String phoneGapversion, Set<ApplicationFeature> features) {
        this.applicationName = applicationName;
        this.applicationPackage = applicationPackage;
        this.applicationVersion = applicationVersion;
        this.phoneGapversion = phoneGapversion;
        this.features = features;
    }

    public ApplicationConfig() {
    }

    public ApplicationConfig(ApplicationConfig applicationConfig) {
        this.applicationName=applicationConfig.applicationName;
        this.applicationPackage = applicationConfig.applicationPackage;
        this.applicationVersion = applicationConfig.applicationVersion;
        this.phoneGapversion = applicationConfig.phoneGapversion;
        this.features = new HashSet<>(applicationConfig.features);
    }

    public String getApplicationPackage() {
        return applicationPackage;
    }

    public void setApplicationPackage(String appPackage) {
        this.applicationPackage = appPackage;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String version) {
        this.applicationVersion = version;
    }

    public String getPhoneGapversion() {
        return phoneGapversion;
    }

    public void setPhoneGapversion(String phoneGapversion) {
        this.phoneGapversion = phoneGapversion;
    }

    @Override
    public String toString() {
        return "ApplicationConfig [appPackage=" + applicationPackage + ", version="
                + applicationVersion + ", phoneGapversion=" + phoneGapversion + ", name="
                + applicationName + "]";
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String name) {
        this.applicationName = name;
    }

    public Set<ApplicationFeature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<ApplicationFeature> features) {
        this.features = features;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationConfig that = (ApplicationConfig) o;

        if (!applicationName.equals(that.applicationName)) return false;
        if (!applicationPackage.equals(that.applicationPackage)) return false;
        if (!applicationVersion.equals(that.applicationVersion)) return false;
        if (!features.equals(that.features)) return false;
        if (!phoneGapversion.equals(that.phoneGapversion)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = applicationPackage.hashCode();
        result = 31 * result + applicationVersion.hashCode();
        result = 31 * result + phoneGapversion.hashCode();
        result = 31 * result + applicationName.hashCode();
        result = 31 * result + features.hashCode();
        return result;
    }
}