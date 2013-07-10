package com.ttech.cordovabuild.infrastructure.security;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.shiro.aop.AnnotationResolver;
import org.apache.shiro.aop.DefaultAnnotationResolver;
import org.apache.shiro.authz.aop.AnnotationsAuthorizingMethodInterceptor;
import org.apache.shiro.authz.aop.AuthenticatedAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.GuestAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.PermissionAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.RoleAnnotationMethodInterceptor;
import org.apache.shiro.authz.aop.UserAnnotationMethodInterceptor;

public class ShiroInterceptor extends AnnotationsAuthorizingMethodInterceptor {
	public ShiroInterceptor() {
		List<AuthorizingAnnotationMethodInterceptor> interceptors = new ArrayList<AuthorizingAnnotationMethodInterceptor>(
				5);

		// use a Spring-specific Annotation resolver - Spring's AnnotationUtils
		// is nicer than the
		// raw JDK resolution process.
		AnnotationResolver resolver = new DefaultAnnotationResolver();
		// we can re-use the same resolver instance - it does not retain state:
		interceptors.add(new RoleAnnotationMethodInterceptor(resolver));
		interceptors.add(new PermissionAnnotationMethodInterceptor(resolver));
		interceptors
				.add(new AuthenticatedAnnotationMethodInterceptor(resolver));
		interceptors.add(new UserAnnotationMethodInterceptor(resolver));
		interceptors.add(new GuestAnnotationMethodInterceptor(resolver));

		setMethodInterceptors(interceptors);
	}

	@AroundInvoke
	public Object manageSecurity(InvocationContext ctx) throws Throwable {
		org.apache.shiro.aop.MethodInvocation mi = createMethodInvocation(ctx);
		return super.invoke(mi);
	}

	protected org.apache.shiro.aop.MethodInvocation createMethodInvocation(
			final InvocationContext ctx) {

		return new org.apache.shiro.aop.MethodInvocation() {
			public Method getMethod() {
				return ctx.getMethod();
			}

			public Object[] getArguments() {
				return ctx.getParameters();
			}

			public String toString() {
				return "Method invocation [" + ctx.getMethod() + "]";
			}

			public Object proceed() throws Throwable {
				return ctx.proceed();
			}

			public Object getThis() {
				return ctx.getTarget();
			}
		};
	}
}
