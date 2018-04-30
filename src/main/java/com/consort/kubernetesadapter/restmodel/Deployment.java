package com.consort.kubernetesadapter.restmodel;

public class Deployment {
  public String getName() {
    return name;
  }

  public String getVersion() {
    return version;
  }

  public String getStatus() {
    return status;
  }

  public String getReason() {
    return reason;
  }

  public int getInstances() {
    return instances;
  }

  private String name;
  private String version;
  private String status;
  private String reason;
  private int instances;

  public Deployment(String name, String version, String status, Integer instances, String reason) {
    this.name = name;
    this.version = version;
    this.status = status;
    this.instances = instances == null?0:instances;
    this.reason = "Reason: '"+reason+"' caused transition to this status.";
  }
}
