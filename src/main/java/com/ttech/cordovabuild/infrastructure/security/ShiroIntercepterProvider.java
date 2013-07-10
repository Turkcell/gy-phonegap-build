package com.ttech.cordovabuild.infrastructure.security;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.interceptor.Interceptors;

import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;
import org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

public class ShiroIntercepterProvider implements Extension {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ShiroIntercepterProvider.class);
	@SuppressWarnings("unchecked")
	private static final Set<Class<? extends Annotation>> AUTHZ_ANNOTATION_CLASSES = Sets
			.newHashSet(RequiresPermissions.class, RequiresRoles.class,
					RequiresUser.class, RequiresGuest.class,
					RequiresAuthentication.class);

	@SuppressWarnings("rawtypes")
	private static final Map<String, Class[]> SHIRO_INTERCEPTORS = new ImmutableMap.Builder<String, Class[]>()
			.put("value", new Class[] { ShiroInterceptor.class }).build();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void processShiroAnnotations(@Observes ProcessAnnotatedType pat) {
		Set<Annotation> annotations = pat.getAnnotatedType().getAnnotations();
		for (Annotation a : annotations) {
			if (AUTHZ_ANNOTATION_CLASSES.contains(a.annotationType())) {
				decorateInterceptor(pat);
				return;
			}
		}
		AnnotatedTypeBuilder<Object> builder = null;
		Set<AnnotatedMethod> methods = pat.getAnnotatedType().getMethods();
		for (AnnotatedMethod annotatedMethod : methods) {
			for (Annotation a : annotatedMethod.getAnnotations()) {
				if (AUTHZ_ANNOTATION_CLASSES.contains(a.annotationType())) {
					if (builder == null)
						builder = new AnnotatedTypeBuilder<Object>()
								.readFromType(pat.getAnnotatedType());

					builder.addToMethod(annotatedMethod,
							AnnotationInstanceProvider.of(Interceptors.class,
									SHIRO_INTERCEPTORS));
					break;
				}
			}
		}
		if (builder != null)
			pat.setAnnotatedType(builder.create());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void decorateInterceptor(ProcessAnnotatedType pat) {
		LOGGER.debug("security annotation found on {}", pat.getAnnotatedType()
				.getJavaClass());
		pat.setAnnotatedType(new AnnotatedTypeBuilder<Object>()
				.readFromType(pat.getAnnotatedType())
				.addToClass(
						AnnotationInstanceProvider.of(Interceptors.class,
								SHIRO_INTERCEPTORS)).create());
	}
}
