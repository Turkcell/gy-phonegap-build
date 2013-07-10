package com.ttech.cordovabuild.web;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.shiro.authz.annotation.RequiresUser;

@Path("/example")
public class Example {
	@Inject
	ExampleService exampleService;

	@GET
	@RequiresUser
	public String getName() {
		return exampleService.getHello();
	}
}
