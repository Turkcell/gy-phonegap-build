/*
 * Copyright © 2013 Turkcell Teknoloji Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ttech.cordovabuild.infrastructure.security;

import java.io.IOException;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.Filter;

import org.apache.commons.io.IOUtils;
import org.apache.deltaspike.core.api.config.ConfigProperty;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.TextConfigurationRealm;
import org.apache.shiro.util.LifecycleUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;

import com.google.common.base.Strings;

@ApplicationScoped
public class ShiroConfig {
	@Inject
	@ConfigProperty(name = "security.loginURL")
	private String loginUrl;
	@Inject
	@ConfigProperty(name = "security.successURL")
	private String successUrl;
	@Inject
	@ConfigProperty(name = "security.unauthorizedURL")
	private String unauthorizedUrl;

	@Produces
	public DefaultWebSecurityManager createSecurityManager(@Text Realm realm) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(
				realm);
		SecurityUtils.setSecurityManager(securityManager);
		return securityManager;
	}

	public void disposeSecurityManager(@Disposes DefaultWebSecurityManager sm) {
		LifecycleUtils.destroy(sm);
	}

	private void applyGlobalPropertiesIfNecessary(Filter filter) {
		applyLoginUrlIfNecessary(filter);
		applySuccessUrlIfNecessary(filter);
		applyUnauthorizedUrlIfNecessary(filter);
	}

	private void applyLoginUrlIfNecessary(Filter filter) {

		if (!Strings.isNullOrEmpty(loginUrl)
				&& (filter instanceof AccessControlFilter)) {
			AccessControlFilter acFilter = (AccessControlFilter) filter;
			// only apply the login url if they haven't explicitly configured
			// one already:
			String existingLoginUrl = acFilter.getLoginUrl();
			if (AccessControlFilter.DEFAULT_LOGIN_URL.equals(existingLoginUrl)) {
				acFilter.setLoginUrl(loginUrl);
			}
		}
	}

	private void applySuccessUrlIfNecessary(Filter filter) {

		if (!Strings.isNullOrEmpty(successUrl)
				&& (filter instanceof AuthenticationFilter)) {
			AuthenticationFilter authcFilter = (AuthenticationFilter) filter;
			// only apply the successUrl if they haven't explicitly configured
			// one already:
			String existingSuccessUrl = authcFilter.getSuccessUrl();
			if (AuthenticationFilter.DEFAULT_SUCCESS_URL
					.equals(existingSuccessUrl)) {
				authcFilter.setSuccessUrl(successUrl);
			}
		}
	}

	private void applyUnauthorizedUrlIfNecessary(Filter filter) {

		if (!Strings.isNullOrEmpty(unauthorizedUrl)
				&& (filter instanceof AuthorizationFilter)) {
			AuthorizationFilter authzFilter = (AuthorizationFilter) filter;
			// only apply the unauthorizedUrl if they haven't explicitly
			// configured one already:
			String existingUnauthorizedUrl = authzFilter.getUnauthorizedUrl();
			if (existingUnauthorizedUrl == null) {
				authzFilter.setUnauthorizedUrl(unauthorizedUrl);
			}
		}
	}

	protected FilterChainManager createFilterChainManager() {
		DefaultFilterChainManager manager = new DefaultFilterChainManager();
		Map<String, Filter> defaultFilters = manager.getFilters();
		// apply global settings if necessary:
		for (Filter filter : defaultFilters.values()) {
			applyGlobalPropertiesIfNecessary(filter);
		}
		return manager;
	}

	@Produces
	@Named("shiroFilter")
	@Default
	public CDIShiroFilter filterProducer(WebSecurityManager manager) {
		FilterChainManager filterChainManager = createFilterChainManager();
		PathMatchingFilterChainResolver chainResolver = new PathMatchingFilterChainResolver();
		chainResolver.setFilterChainManager(filterChainManager);
		CDIShiroFilter cdiShiroFilter = new CDIShiroFilter(manager,
				chainResolver);
		LifecycleUtils.init(cdiShiroFilter);
		return cdiShiroFilter;
	}

	void destroyShiroFilter(@Disposes CDIShiroFilter cdiShiroFilter) {
		LifecycleUtils.destroy(cdiShiroFilter);
	}

	@Text
	@Produces
	public TextConfigurationRealm createTextRealm() {
		TextConfigurationRealm realm = new TextConfigurationRealm();
		realm.setUserDefinitions(readUserDefinitions());
		realm.init();
		return realm;
	}

	private String readUserDefinitions() {
		try {
			return IOUtils.toString(this.getClass().getClassLoader()
					.getResourceAsStream("users.txt"));
		} catch (IOException e) {
			throw new ConfigurationException(e);
		}
	}
}
