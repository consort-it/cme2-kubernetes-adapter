package com.consort.kubernetesadapter.exception;

import com.consort.kubernetesadapter.restmodel.Errors;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.sql.Timestamp;
import java.util.List;

@JsonIgnoreProperties({"cause", "stackTrace", "localizedMessage", "suppressed"})
public class KubernetesException extends Exception {
  private int status;
  private String code;
  private String message;

  public int getStatus() {
    return status;
  }
  public String getCode() {
    return code;
  }
  public String getMessage() {
    return message;
  }
  public String getLocation() {
    return location;
  }
  public Timestamp getTime() {
    return time;
  }
  public StackTraceElement[] getTrace() {
    return trace;
  }

  private String location;
  private Timestamp time;
  private StackTraceElement[] trace;

  public KubernetesException(Errors err, String location, Exception originalException) {
    super(err.getMessage(), originalException);
    this.status = err.getStatus();
    this.code = err.getCode();
    this.message = err.getMessage();
    this.location = location;
    this.trace = this.getStackTrace();
    this.time = new Timestamp(System.currentTimeMillis());
  }
  public KubernetesException(Errors err, String location) {
    super(err.getMessage());
    this.status = err.getStatus();
    this.code = err.getCode();
    this.message = err.getMessage();
    this.location = location;
    this.trace = this.getStackTrace();
    this.time = new Timestamp(System.currentTimeMillis());
  }
}
