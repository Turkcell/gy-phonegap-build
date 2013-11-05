package com.ttech.cordovabuild.web.exception;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class UnauthenticatedExceptionMapper extends
        AbstractExceptionMapper<AuthenticationException> {


    @Override
    public Response toResponse(AuthenticationException exception) {
        return super.toResponse(Status.UNAUTHORIZED, exception);
    }

}
