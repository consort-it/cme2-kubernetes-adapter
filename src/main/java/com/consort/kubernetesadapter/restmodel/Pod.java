package com.consort.kubernetesadapter.restmodel;


public class Pod {
  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public String getStatus() {
    return status;
  }

  private String name;
  private String version;
  private String status;

  public Pod(String name, String version, String status) {
    this.name = name;
    this.version = version;
    this.status = status;
  }


}
