package com.consort.kubernetesadapter.utils;


import com.consort.kubernetesadapter.restmodel.States;

public class DeploymentState {
  private States state;
  private String reason;

  public DeploymentState(States state, String reason) {
    this.state = state;
    this.reason = reason;
  }

  public String getStatus() {
    return this.state.getStatus();
  }

  public String getReason() {
    return this.reason;
  }
}
