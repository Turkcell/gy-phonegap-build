/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ttech.cordovabuild.web;

import com.ttech.cordovabuild.domain.application.Application;
import com.ttech.cordovabuild.domain.application.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;

@Component
public class ApplicationResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationResource.class);
    @Autowired
    ApplicationService service;

    @GET
    @Path("/{id}")
    public Application getApplication(@PathParam("id") Long id) {
        LOGGER.info("get application with {} and application service {}", id, service);
        return service.findApplication(id);
    }

    @PUT
    @Path("/{id}")
    public Application updateApplication(@PathParam("id") Long id) {
        return service.updateApplicationCode(id);
    }

    @POST
    public Application createApplication(@QueryParam("sourceUri") String sourceUri) {
        return service.createApplication(SecurityContextHolder.getContext().getAuthentication().getName(), sourceUri);
    }
}
