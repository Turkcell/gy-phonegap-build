package com.ttech.cordovabuild.web.exception;

import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class UnauthenticatedExceptionMapper extends
        AbstractExceptionMapper<AuthenticationException> {
    @Context
    HttpHeaders hh;

    @Context
    protected HttpServletRequest httpServletRequest;

    @Override
    protected HttpHeaders getHh() {
        return hh;
    }

    @Override
    public Response toResponse(AuthenticationException exception) {
        return super.toResponse(Status.UNAUTHORIZED, exception);
    }

    @Override
    protected HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

}
