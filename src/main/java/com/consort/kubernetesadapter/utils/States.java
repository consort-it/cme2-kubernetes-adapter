package com.consort.kubernetesadapter.utils;

import java.util.Arrays;

public enum States {
  UNDEFINED("Broken"),
  BROKEN("Broken"),
  INITIALIZING("Initializing"),
  STOPPED("Stopped"),
  RUNNING("Running");

  private String status;

  States(String ...error) {
    this.status = Arrays.asList(error).get(0);
  }

  public String getStatus() {
    return status;
  }
}
