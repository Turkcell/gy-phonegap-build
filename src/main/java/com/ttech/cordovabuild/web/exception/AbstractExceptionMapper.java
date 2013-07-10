package com.ttech.cordovabuild.web.exception;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.OK;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.ttech.cordovabuild.web.ApiResponse;

public abstract class AbstractExceptionMapper<E extends java.lang.Throwable>
		implements ExceptionMapper<E> {

	public static final Logger logger = LoggerFactory
			.getLogger(AbstractExceptionMapper.class.getPackage().toString());
	static Map<String, Set<String>> javascriptTypes = new HashMap<String, Set<String>>();
	static ObjectMapper mapper = new ObjectMapper();

	static {
		// application/javascript, application/x-javascript, text/ecmascript,
		// application/ecmascript, text/jscript
		javascriptTypes.put(
				"application",
				new HashSet<String>(Arrays.asList("x-javascript", "ecmascript",
						"javascript")));
		javascriptTypes.put("text",
				new HashSet<String>(Arrays.asList("ecmascript", "jscript")));
	}
	@Context
	HttpHeaders hh;

	@Context
	protected HttpServletRequest httpServletRequest;

	public boolean isJSONP() {
		return isJavascript(hh.getAcceptableMediaTypes());
	}

	public static boolean isJavascript(MediaType m) {
		if (m == null) {
			return false;
		}

		Set<String> subtypes = javascriptTypes.get(m.getType());
		if (subtypes == null) {
			return false;
		}

		return subtypes.contains(m.getSubtype());
	}

	public static boolean isJavascript(List<MediaType> l) {
		for (MediaType m : l) {
			if (isJavascript(m)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Response toResponse(E e) {
		return toResponse(BAD_REQUEST, e);
	}

	public Response toResponse(Status status, E e) {
		return toResponse(status.getStatusCode(), e);
	}

	public Response toResponse(int status, E e) {
		logger.error("Error in request (" + status + ")", e);
		ApiResponse response = new ApiResponse(
				httpServletRequest.getServletPath());
		AuthErrorInfo authError = AuthErrorInfo.getForException(e);
		if (authError != null) {
			response.setError(authError.getType(), authError.getMessage(), e);
		} else {
			response.setError(e);
		}
		String jsonResponse = mapToJsonString(response);
		return toResponse(status, jsonResponse);
	}

	public Response toResponse(Status status, String jsonResponse) {
		return toResponse(status.getStatusCode(), jsonResponse);
	}

	public Response toResponse(int status, String jsonResponse) {
		logger.error("Error in request (" + status + "):\n" + jsonResponse);
		String callback = httpServletRequest.getParameter("callback");
		if (isJSONP() && !Strings.isNullOrEmpty(callback)) {
			jsonResponse = wrapJSONPResponse(callback, jsonResponse);
			return Response.status(OK).type("application/javascript")
					.entity(jsonResponse).build();
		} else {
			return Response.status(status).type(APPLICATION_JSON_TYPE)
					.entity(jsonResponse).build();

		}
	}

	public static String wrapJSONPResponse(String callback, String jsonResponse) {
		if (!Strings.isNullOrEmpty(callback)) {
			return callback + "(" + jsonResponse + ")";
		} else {
			return jsonResponse;

		}
	}
	public static String mapToJsonString(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}
		return "{}";

	}
}
