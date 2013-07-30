/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ttech.cordovabuild.web;

import com.ttech.cordovabuild.domain.Application;
import com.ttech.cordovabuild.domain.ApplicationRepository;
import javax.ws.rs.POST;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class ApplicationResource {

    @Autowired
    ApplicationRepository repository;

    @POST
    public Application createApplication(Application app) {
        return repository.saveApplication(app);
    }
}
