/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ttech.cordovabuild.web;

import com.ttech.cordovabuild.domain.application.Application;
import com.ttech.cordovabuild.domain.application.ApplicationRepository;
import javax.ws.rs.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationResource {

    @Autowired
    ApplicationRepository repository;

    @POST
    public Application createApplication(Application app) {
        return repository.saveApplication(app);
    }
}
