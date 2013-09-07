/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ttech.cordovabuild.web;

import javax.ws.rs.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ttech.cordovabuild.domain.application.Application;
import com.ttech.cordovabuild.domain.application.ApplicationService;

@Component
public class ApplicationResource {

    @Autowired
    ApplicationService service;

    @GET
    @Path("/{id}")
    public Application getApplication(@PathParam("id") Long id) {
        return service.findApplication(id);
    }

    @POST
    public Application createApplication(@QueryParam("sourceUri") String sourceUri) {
        return null;
    }
}
