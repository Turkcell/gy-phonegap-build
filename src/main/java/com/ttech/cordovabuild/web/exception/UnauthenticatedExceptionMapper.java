package com.ttech.cordovabuild.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

import org.apache.shiro.authz.UnauthenticatedException;

@Provider
public class UnauthenticatedExceptionMapper extends
		AbstractExceptionMapper<UnauthenticatedException> {

	@Override
	public Response toResponse(UnauthenticatedException exception) {
		return super.toResponse(Status.UNAUTHORIZED,exception);
	}

}
