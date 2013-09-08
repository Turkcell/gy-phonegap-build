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

package com.ttech.cordovabuild.web.exception;

import com.ttech.cordovabuild.domain.application.EntityNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * Created with IntelliJ IDEA.
 * User: capacman
 * Date: 9/8/13
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {

    @Override
    public Response toResponse(EntityNotFoundException e) {
        return Response.status(Status.NOT_FOUND).type(APPLICATION_JSON_TYPE).build();
    }
}
