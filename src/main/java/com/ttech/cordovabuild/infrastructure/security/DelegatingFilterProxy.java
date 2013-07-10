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
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class DelegatingFilterProxy implements Filter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DelegatingFilterProxy.class);

	private String contextAttribute;

	private BeanManager beanManager;

	private String targetBeanName;

	private boolean targetFilterLifecycle = false;

	private Filter delegate;

	private final Object delegateMonitor = new Object();

	private FilterConfig filterConfig;

	public DelegatingFilterProxy() {
	}

	public DelegatingFilterProxy(Filter delegate) {
		Preconditions.checkNotNull(delegate,
				"delegate Filter object must not be null");
		this.delegate = delegate;
	}

	public DelegatingFilterProxy(String targetBeanName) {
		this(targetBeanName, null);
	}

	public DelegatingFilterProxy(String targetBeanName, BeanManager bm) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(targetBeanName),
				"target Filter bean name must not be null or empty");
		this.setTargetBeanName(targetBeanName);
		this.beanManager = bm;
	}

	public void setContextAttribute(String contextAttribute) {
		this.contextAttribute = contextAttribute;
	}

	public String getContextAttribute() {
		return this.contextAttribute;
	}

	public void setTargetBeanName(String targetBeanName) {
		this.targetBeanName = targetBeanName;
	}

	protected String getTargetBeanName() {
		return this.targetBeanName;
	}

	public void setTargetFilterLifecycle(boolean targetFilterLifecycle) {
		this.targetFilterLifecycle = targetFilterLifecycle;
	}

	protected boolean isTargetFilterLifecycle() {
		return this.targetFilterLifecycle;
	}

	protected void initFilterBean() throws ServletException {
		synchronized (this.delegateMonitor) {
			if (this.delegate == null) {
				// If no target bean name specified, use filter name.
				if (this.targetBeanName == null) {
					this.targetBeanName = getFilterName();
				}

				// Fetch Spring root application context and initialize the
				// delegate early,
				// if possible. If the root application context will be started
				// after this
				// filter proxy, we'll have to resort to lazy initialization.
				BeanManager bm = findBeanManager();
				if (bm != null) {
					this.delegate = initDelegate(bm);
				}
			}
		}
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		// Lazily initialize the delegate if necessary.
		Filter delegateToUse = null;
		synchronized (this.delegateMonitor) {
			if (this.delegate == null) {
				BeanManager bm = findBeanManager();
				if (bm == null) {
					throw new IllegalStateException(
							"No BeanManager found: no ContextLoaderListener registered?");
				}
				this.delegate = initDelegate(bm);
			}
			delegateToUse = this.delegate;
		}

		// Let the delegate perform the actual doFilter operation.
		invokeDelegate(delegateToUse, request, response, filterChain);
	}

	private BeanManager findBeanManager() {
		if (this.beanManager == null) {
			this.beanManager = CDI.current().getBeanManager();
		}
		return this.beanManager;

	}

	@Override
	public void destroy() {
		Filter delegateToUse = null;
		synchronized (this.delegateMonitor) {
			delegateToUse = this.delegate;
		}
		if (delegateToUse != null) {
			destroyDelegate(delegateToUse);
		}
	}

	protected Filter initDelegate(BeanManager bm) throws ServletException {
		Set<Bean<?>> beans = bm.getBeans(Filter.class);
		Filter delegate = null;
		for (Bean<?> bean : beans) {
			if (bean.getName().equals(getTargetBeanName())) {
				delegate = (Filter) beanManager
						.getReference(bean, Filter.class,
								beanManager.createCreationalContext(bean));
				break;
			}
		}

		if (isTargetFilterLifecycle()) {
			delegate.init(getFilterConfig());
		}
		return delegate;
	}

	protected void invokeDelegate(Filter delegate, ServletRequest request,
			ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		delegate.doFilter(request, response, filterChain);
	}

	protected void destroyDelegate(Filter delegate) {
		if (isTargetFilterLifecycle()) {
			delegate.destroy();
		}
	}

	private FilterConfig getFilterConfig() {
		return filterConfig;
	}

	private String getFilterName() {
		return this.filterConfig != null ? this.filterConfig.getFilterName()
				: null;
	}

	public final void init(FilterConfig filterConfig) throws ServletException {
		Preconditions.checkNotNull(filterConfig,
				"FilterConfig must not be null");

		LOGGER.debug("Initializing filter '{}'", filterConfig.getFilterName());

		this.filterConfig = filterConfig;

		this.targetFilterLifecycle = this.filterConfig
				.getInitParameter("targetFilterLifecycle") == null ? false
				: Boolean.valueOf(this.filterConfig
						.getInitParameter("targetFilterLifecycle"));

		// Let subclasses do whatever initialization they like.
		initFilterBean();

		LOGGER.debug("Filter '{}' configured successfully",
				filterConfig.getFilterName());

	}

}