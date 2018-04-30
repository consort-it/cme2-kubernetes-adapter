package com.consort.kubernetesadapter.restmodel;

import java.util.Arrays;

public enum Errors {
  ERR_UNKNOWN_ERROR("500", "KA-0", "Unmapped Error ocurred! Aborted."),
  ERR_CONNECTING("500", "KA-1", "No Kubernetes Connection available! Aborted."),
  ERR_UNDEFINED_NAMESPACE("400", "KA-2", "Namespace is undefined! Aborted."),
  ERR_UNDEFINED_SERVICE("400", "KA-3", "Service is undefined! Aborted."),
  ERR_NO_SUCH_SERVICE("400", "KA-4", "Service can't be found! Aborted."),
  ERR_UNDEFINED_DEPLOYMENT("400", "KA-5", "Deployment is undefined! Aborted."),
  ERR_NO_SUCH_DEPLOYMENT("400", "KA-6", "Deployment can't be found! Aborted."),
  ERR_AUTHENTICATION_REQUIRED("401", "KA-999", "Authentitcation is missing."),
  ERR_FORBIDDEN("403", "KA-998", "Your Token is forbidden to use."),
  ERR_PAGE_NOT_FOUND("404", "KA-999", "Page could not be found."),
  ERR_NOT_FOUND("404", "KA-999", "Requested URI is not available! Aborted.");

  private Integer status;
  private String code;
  private String message;

  Errors(String ...error) {
    this.status = Integer.parseInt(Arrays.asList(error).get(0));
    this.code = Arrays.asList(error).get(1);
    this.message = Arrays.asList(error).get(2);
  }

  public Integer getStatus() {
    return status;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
