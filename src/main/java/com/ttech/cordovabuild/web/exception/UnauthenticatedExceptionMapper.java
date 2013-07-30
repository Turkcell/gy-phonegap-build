package com.ttech.cordovabuild.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;
import org.springframework.security.core.AuthenticationException;

@Provider
public class UnauthenticatedExceptionMapper extends
        AbstractExceptionMapper<AuthenticationException> {

    @Override
    public Response toResponse(AuthenticationException exception) {
        return super.toResponse(Status.UNAUTHORIZED, exception);
    }

}
