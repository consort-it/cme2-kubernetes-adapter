package com.consort.kubernetesadapter.utils;

public enum StatusMessages {
  ERROR_RETRIEVING_PODS("Error retrieving pods!"),
  ERROR_RETRIEVING_SERVICES("Error retrieving services!"),
  ERROR_RETRIEVING_DEPLOYMENTS("Error retrieving deployments!");

  private String value;

  StatusMessages(final String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
