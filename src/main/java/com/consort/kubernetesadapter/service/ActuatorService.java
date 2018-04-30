package com.consort.kubernetesadapter.service;

import spark.Response;

import java.util.ArrayList;
import java.util.List;

public class ActuatorService {

  private static ActuatorService instance = null;

  private ActuatorService() {}

  public static ActuatorService getInstance() {
    if(instance == null) {
      instance = new ActuatorService();
    }

    return instance;
  }

  public List<Integer> getCounters(final Response response) {
    response.type("application/json");

    List<Integer> counterList = new ArrayList<Integer>();

    return counterList;
  }

  public Integer getCounterByName(final String name) {
    return 2;
  }
}
