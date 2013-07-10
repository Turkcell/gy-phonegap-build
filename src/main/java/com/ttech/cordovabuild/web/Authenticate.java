package com.ttech.cordovabuild.web;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.RememberMeAuthenticationToken;
import org.apache.shiro.subject.Subject;

@Path("/authenticate")
@Consumes(MediaType.APPLICATION_JSON)
public class Authenticate {

	public class UsernamePasswordCredentials implements
			RememberMeAuthenticationToken, AuthenticationToken {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8751115854398154081L;
		private String username;
		private String password;
		private boolean rememberMe;

		public UsernamePasswordCredentials() {
		}

		public UsernamePasswordCredentials(String username, String password) {
			this.username = username;
			this.password = password;
		}

		public UsernamePasswordCredentials(String username, String password,
				boolean rememberMe) {
			this.username = username;
			this.password = password;
			this.rememberMe = rememberMe;
		}

		@Override
		public Object getPrincipal() {
			return username;
		}

		@Override
		public Object getCredentials() {
			return password;
		}

		@Override
		public boolean isRememberMe() {
			return rememberMe;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public void setRememberMe(boolean rememberMe) {
			this.rememberMe = rememberMe;
		}

	}

	@POST
	@Path("/login")
	public void login(UsernamePasswordCredentials token) {
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
	}

	@POST
	@Path("logout")
	public void logout() {
		SecurityUtils.getSubject().logout();
	}
}
