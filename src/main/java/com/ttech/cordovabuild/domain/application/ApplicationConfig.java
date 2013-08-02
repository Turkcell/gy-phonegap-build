package com.ttech.cordovabuild.domain.application;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ApplicationConfig {

	@Column(length = 1024)
	@Basic
	public String appPackage;
	@Column(length = 200)
	@Basic
	public String version;
	@Column(length = 200)
	@Basic
	public String phoneGapversion;

	public ApplicationConfig(String appPackage, String version,
			String phoneGapversion) {
		this.appPackage = appPackage;
		this.version = version;
		this.phoneGapversion = phoneGapversion;
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
}