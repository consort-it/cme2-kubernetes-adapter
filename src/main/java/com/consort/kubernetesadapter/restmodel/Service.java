package com.consort.kubernetesadapter.restmodel;


public class Service {
  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public String getStatus() {
    return status;
  }

  public int getInstances() {
    return instances;
  }

  private String name;
  private String version;
  private String status;
  private int instances;

  public Service(String name, String version, String status, Integer instances) {
    this.name = name;
    this.version = version;
    this.status = status;
    this.instances = instances;
  }
}
