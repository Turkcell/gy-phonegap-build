package com.ttech.cordovabuild.infrastructure;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class Startup {
	private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);

	public void startup(
			@Observes @Initialized(ApplicationScoped.class) ServletContext sc) {
		LOGGER.info("application started");
	}
}
