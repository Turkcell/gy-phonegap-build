package com.ttech.cordovabuild.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApiResponse {

    private String error;
    private String errorDescription;
    private String errorUri;
    private String exception;
    private String callback;

    private String path;
    private String uri;
    private String status;
    private long timestamp;
    private String cursor;
    private Integer count;
    private String action;
    private Map<String, List<String>> params;
    private Object data;
    private String basePath;

    public ApiResponse(String basePath) {
        timestamp = System.currentTimeMillis();
        this.basePath = basePath;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getError() {
        return error;
    }

    public void setError(String code) {
        error = code;
    }

    public static String exceptionToErrorCode(Throwable e) {
        if (e == null) {
            return "service_error";
        }
        String s = e.getClass().getSimpleName();
        if (s.endsWith("Exception"))
            s = s.subSequence(0, s.lastIndexOf("Exception")).toString();
        return s;
    }

    public ApiResponse withError(String code) {
        return withError(code, null, null);
    }

    public void setError(Throwable e) {
        setError(null, null, e);
    }

    public ApiResponse withError(Throwable e) {
        return withError(null, null, e);
    }

    public void setError(String description, Throwable e) {
        setError(null, description, e);
    }

    public ApiResponse withError(String description, Throwable e) {
        return withError(null, description, e);
    }

    public void setError(String code, String description, Throwable e) {
        if (code == null) {
            code = exceptionToErrorCode(e);
        }
        error = code;
        errorDescription = description;
        if (e != null) {
            if (description == null) {
                errorDescription = e.getMessage();
            }
            exception = e.getClass().getName();
        }
    }

    public ApiResponse withError(String code, String description, Throwable e) {
        setError(code, description, e);
        return this;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("error_description")
    public String getErrorDescription() {
        return errorDescription;
    }

    @JsonProperty("error_description")
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    @JsonProperty("error_uri")
    public String getErrorUri() {
        return errorUri;
    }

    @JsonProperty("error_uri")
    public void setErrorUri(String errorUri) {
        this.errorUri = errorUri;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (path == null) {
            this.path = null;
            uri = null;
        }
        this.path = path;
        uri = basePath + path;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getUri() {
        return uri;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getStatus() {
        return status;
    }

    public void setSuccess() {
        status = "ok";
    }

    public ApiResponse withSuccess() {
        status = "ok";
        return this;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public long getDuration() {
        return System.currentTimeMillis() - timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ApiResponse withTimestamp(long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public long getTimestamp() {
        return timestamp;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ApiResponse withAction(String action) {
        this.action = action;
        return this;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        if (data != null) {
            this.data = data;
        } else {
            this.data = new LinkedHashMap<String, Object>();
        }
    }

    public ApiResponse withData(Object data) {
        setData(data);
        return this;
    }

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public Map<String, List<String>> getParams() {
        return params;
    }

    public void setParams(Map<String, List<String>> params) {
        Map<String, List<String>> q = new LinkedHashMap<String, List<String>>();
        for (String k : params.keySet()) {
            List<String> v = params.get(k);
            if (v != null) {
                q.put(k, new ArrayList<String>(v));
            }
        }
        this.params = q;
    }

}
