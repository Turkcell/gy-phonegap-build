/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ttech.cordovabuild.domain.user;

import org.springframework.security.core.GrantedAuthority;

/**
 *
 * @author capacman
 */
public enum Role implements GrantedAuthority {

    USER, ANONYMOUS, ADMIN;

    @Override
    public String getAuthority() {
        return USER.name();
    }

}
