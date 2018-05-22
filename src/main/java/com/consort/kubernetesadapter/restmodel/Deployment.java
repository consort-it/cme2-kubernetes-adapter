package com.consort.kubernetesadapter.restmodel;

import com.consort.kubernetesadapter.restmodel.kubernetes.Env;

import java.util.LinkedList;
import java.util.List;

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

  public List getConfig() {
    List result = new LinkedList();
    for(int index = 0; index < this.env.size(); index++) {
      if (!this.env.get(index).isSecret())
        result.add(this.env.get(index));
    }
    return result;
  }

  private String name;
  private String version;
  private String status;
  private String reason;
  private int instances;
  private List<Env> env;

  public Deployment(String name, String version, String status, Integer instances, String reason, List<Env> env) {
    this.name = name;
    this.version = version;
    this.status = status;
    this.instances = instances == null?0:instances;
    this.reason = "Reason: '"+reason+"' caused transition to this status.";
    this.env = env;
  }
}
