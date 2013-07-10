package com.ttech.cordovabuild.web.exception;

import org.apache.shiro.authz.UnauthenticatedException;

public enum AuthErrorInfo {

	INVALID_AUTH_ERROR("auth_invalid", "Unable to authenticate");

	private final String type;
	private final String message;

	AuthErrorInfo(String type, String message) {
		this.type = type;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public static AuthErrorInfo getForException(Throwable e) {
		if (e instanceof UnauthenticatedException) {
			return INVALID_AUTH_ERROR;
		}
		return null;
	}
}
