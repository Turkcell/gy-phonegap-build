package com.ttech.cordovabuild.web;

import com.ttech.cordovabuild.domain.application.ApplicationService;
import com.ttech.cordovabuild.domain.user.UserRepository;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Component
@Path("/")
@Scope(WebApplicationContext.SCOPE_REQUEST)
@Produces({MediaType.APPLICATION_JSON})
public class RootResource {
    public static class Credentials {

        @NotEmpty
        private String username;
        @NotEmpty
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Autowired
    ApplicationService applicationService;

    @Context
    ResourceContext resourceContext;

    @Autowired
    @Qualifier("authenticationManager")
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityContextRepository securityContextRepository;

    @Autowired
    UserRepository userRepository;


    @Path("application")
    public ApplicationResource getApplication() {
        return resourceContext.getResource(ApplicationResource.class);
    }

    @Path("user")
    public UserResource getUserResource() {
        return resourceContext.getResource(UserResource.class);
    }

    @POST
    @Path("login")
    public Response authenticate(@Valid Credentials credentials, @Context HttpServletRequest request, @Context HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
            return Response.accepted().entity(userRepository.findUserByUserName(credentials.getUsername())).build();
        } catch (AuthenticationException e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @GET
    @Path("/{id}/hello")
    public String getHello(@Context UriInfo uriInfo) {
        return uriInfo.getRequestUriBuilder().path("1").build().toString();
    }

}
