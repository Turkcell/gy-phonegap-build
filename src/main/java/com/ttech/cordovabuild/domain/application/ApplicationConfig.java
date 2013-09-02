package com.ttech.cordovabuild.domain.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Set;

@Embeddable
public class ApplicationConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);
    @Column(length = 1024)
    @Basic
    private String appPackage;
    @Column(length = 200)
    @Basic
    private String version;
    @Column(length = 200)
    @Basic
    private String phoneGapversion;
    @Column(length = 1024)
    @Basic
    private String name;
    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<ApplicationFeature> features;

    public ApplicationConfig(String name, String appPackage, String version,
                             String phoneGapversion, Set<ApplicationFeature> features) {
        this.name = name;
        this.appPackage = appPackage;
        this.version = version;
        this.phoneGapversion = phoneGapversion;
        this.features= features;
    }

    public ApplicationConfig() {
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPhoneGapversion() {
        return phoneGapversion;
    }

    public void setPhoneGapversion(String phoneGapversion) {
        this.phoneGapversion = phoneGapversion;
    }

    @Override
    public String toString() {
        return "ApplicationConfig [appPackage=" + appPackage + ", version="
                + version + ", phoneGapversion=" + phoneGapversion + ", name="
                + name + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ApplicationFeature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<ApplicationFeature> features) {
        this.features = features;
    }
}