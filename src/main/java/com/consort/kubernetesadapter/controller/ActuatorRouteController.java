package com.consort.kubernetesadapter.controller;

import com.consort.kubernetesadapter.service.ActuatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Service;

import static spark.Service.ignite;

public class ActuatorRouteController implements RouteController {

  public void initRoutes() {
    final Service http = ignite().port(8081);
    http.get("/api/v1/kubernetes-adapter/health", (req, res) -> {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(null);
    });
    http.get("/api/v1/kubernetes-adapter/metrics", (req, res) -> {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(ActuatorService.getInstance().getCounters(res));
    });
    http.get("/api/v1/kubernetes-adapter/metrics/:name", (req, res) -> {
      final ObjectMapper mapper = new ObjectMapper();
      return mapper.writeValueAsString(ActuatorService.getInstance().getCounterByName(req.params("name")));
    });
  }
}
