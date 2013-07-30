package com.ttech.cordovabuild.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@Component
@Path("/example")
public class Example {

    @Autowired
    ExampleService exampleService;

    @GET
    @Secured(value = "USER")
    public String getName() {
        return exampleService.getHello();
    }
}
