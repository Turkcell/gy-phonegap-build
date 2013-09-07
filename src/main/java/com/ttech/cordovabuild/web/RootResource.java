package com.ttech.cordovabuild.web;

import com.ttech.cordovabuild.domain.application.Application;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;

import com.ttech.cordovabuild.domain.application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
@Component
@Path("/")
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RootResource {

    @Autowired
    ApplicationService applicationService;

    @Context
    ResourceContext resourceContext;

    @Path("/application")
    public ApplicationResource getApplication() {
        return resourceContext.getResource(ApplicationResource.class);
    }

    @Path("/user")
    public UserResource getUserResource(){
        return resourceContext.getResource(UserResource.class);
    }

    @GET
    @Path("hello")
    public String getHello(){
        return "hello";
    }
}
