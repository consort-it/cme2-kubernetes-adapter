package com.consort.kubernetesadapter.restmodel;

public class Namespace {
  private String name;
  private String id;

  public Namespace(String id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public String getId() {
    return this.id;
  }
}
