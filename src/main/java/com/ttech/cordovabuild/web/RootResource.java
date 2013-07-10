package com.ttech.cordovabuild.web;

import com.ttech.cordovabuild.domain.Application;
import com.ttech.cordovabuild.domain.ApplicationRepository;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/")
public class RootResource {

    @Inject
    ApplicationRepository applicationRepository;

    @GET
    @Path("/application/{id}")
    Application getApplication(@PathParam("id") Long id) {
        return applicationRepository.findById(id);
    }
}
