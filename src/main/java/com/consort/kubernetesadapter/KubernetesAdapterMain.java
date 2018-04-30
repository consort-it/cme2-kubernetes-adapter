package com.consort.kubernetesadapter;

import com.consort.kubernetesadapter.controller.ActuatorRouteController;
import com.consort.kubernetesadapter.controller.KubeAPIController;
import com.consort.kubernetesadapter.controller.RouteController;

import java.util.HashSet;
import java.util.Set;

public class KubernetesAdapterMain {

  private static Set<RouteController> routeControllers = new HashSet<>();

  public static void main(String[] args) {
    registerRouteControllers();
    initRoutes();
  }

  private static void registerRouteControllers() {
    routeControllers.add(new KubeAPIController());
    routeControllers.add(new ActuatorRouteController());
  }

  private static void initRoutes() {
    for(final RouteController routeController : routeControllers) {
      routeController.initRoutes();
    }
  }
}


