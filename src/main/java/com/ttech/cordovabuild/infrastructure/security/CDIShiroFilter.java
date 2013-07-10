package com.ttech.cordovabuild.infrastructure.security;

import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class CDIShiroFilter extends AbstractShiroFilter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(CDIShiroFilter.class);

	public CDIShiroFilter(WebSecurityManager wsm, FilterChainResolver resolver) {
		super();
		LOGGER.info("CDIShiroFilter init");
		Preconditions.checkNotNull(wsm,
				"WebSecurityManager property cannot be null.");
		setSecurityManager(wsm);
		if (resolver != null)
			setFilterChainResolver(resolver);
	}

}
