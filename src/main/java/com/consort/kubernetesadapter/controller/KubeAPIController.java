package com.consort.kubernetesadapter.controller;

import com.consort.kubernetesadapter.exception.KubernetesException;
import com.consort.kubernetesadapter.restmodel.Deployment;
import com.consort.kubernetesadapter.restmodel.Errors;
import com.consort.kubernetesadapter.restmodel.Namespace;
import com.consort.kubernetesadapter.security.AuthorizationFilter;
import com.consort.kubernetesadapter.service.KubernetesAPIService;
import com.consort.kubernetesadapter.utils.DeploymentState;
import com.consort.kubernetesadapter.restmodel.States;
import com.consort.kubernetesadapter.utils.RoutingUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.extensions.DeploymentCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import static spark.Service.ignite;

public class KubeAPIController implements RouteController {
  private final static Logger logger = LoggerFactory.getLogger(KubeAPIController.class);

  private static final String SERVICE_PARAM = "service";
  private static final String NAMESPACE_PARAM = "namespace";
  private static final String FILTERBY_OPT_PARAM = "filterBy";

  private static final String NAMESPACES_PATH = "/api/v1/kubernetes-adapter/namespaces";
  private static final String DEPLOYMENTS_PATH = "/api/v1/kubernetes-adapter/namespaces/:namespace/services";
  private static final String DEPLOYMENT_PATH = "/api/v1/kubernetes-adapter/namespaces/:namespace/services/:service";
  private static final String PODS_PATH = "/api/v1/kubernetes-adapter/namespaces/:namespace/services/:service/pods";

  private static final String AUTHORIZER_NAME = "scope";
  private static final String ROLE_ADMIN = "aws.cognito.signin.user.admin";

  public void initRoutes() {
    final Service http = ignite().port(8080);
    RoutingUtils.enableCORS(http, "*", "GET, OPTIONS", "Content-Type, Authorization");

    http.notFound((req, res) -> {
      return getMapper().writeValueAsString(new KubernetesException(Errors.ERR_NOT_FOUND, "InitRoutes: "+req.queryParams()+req.params()+req.url()+req.host()+req.port()+" not Found."));
    });

    http.internalServerError((req, res) -> {
      return getMapper().writeValueAsString(new KubernetesException(Errors.ERR_UNKNOWN_ERROR, "InitRoutes: Unknown Error."));
    });

    handleService(http);
    handleServices(http);
    handleNamespaces(http);
    handlePods(http);
  }

  private void handleNamespaces(final Service http) {
    try {
      http.before(NAMESPACES_PATH, new AuthorizationFilter(AUTHORIZER_NAME, ROLE_ADMIN));
    } catch(KubernetesException e) {
      logger.error("Error on initializing. AuthorizationFilter not working properly.", e);
      System.exit(e.getStatus());
    }
    http.get(NAMESPACES_PATH, (request, response) -> {
      List<Namespace> result = new LinkedList<>();
      try {
        List<io.fabric8.kubernetes.api.model.Namespace> list = KubernetesAPIService.getInstance().getNamespaces();
        Iterator<io.fabric8.kubernetes.api.model.Namespace> it = list.iterator();
        io.fabric8.kubernetes.api.model.Namespace item;

        while(it.hasNext()) {
          item = it.next();

          result.add(new com.consort.kubernetesadapter.restmodel.Namespace(item.getMetadata().getName(), item.getMetadata().getName()));
        }

        return getMapper().writeValueAsString(result);
      } catch (KubernetesException e) {
        response.status(e.getStatus());
        return getMapper().writeValueAsString(e);
      }
    });
  }
  private void handleService(final Service http) {
    try {
      http.before(DEPLOYMENT_PATH, new AuthorizationFilter(AUTHORIZER_NAME, ROLE_ADMIN));
    } catch(KubernetesException e) {
      logger.error("Error on initializing. AuthorizationFilter not working properly.", e);
      System.exit(e.getStatus());
    }
    http.get(DEPLOYMENT_PATH, (request, response) -> {
      final String namespace = request.params(NAMESPACE_PARAM);
      final String service = request.params(SERVICE_PARAM);
      com.consort.kubernetesadapter.restmodel.Deployment result;
      try {
        io.fabric8.kubernetes.api.model.extensions.Deployment item = KubernetesAPIService.getInstance().getDeployment(namespace, service);

        String[] image = {"NA", "NA"};
        image = item.getSpec().getTemplate().getSpec().getContainers().get(0).getImage().split(":");
        DeploymentState cond = this.selectDeploymentCondition(item, item.getStatus().getReplicas());

        String config = item.getMetadata().getAnnotations().get("kubectl.kubernetes.io/last-applied-configuration");
        ObjectMapper mapper = new ObjectMapper();
        com.consort.kubernetesadapter.restmodel.kubernetes.Deployment deployment;

        result = new Deployment(item.getMetadata().getName(), image[image.length-1], cond.getStatus(), item.getStatus().getReplicas()==null?0:item.getStatus().getReplicas(), cond.getReason(), new LinkedList<>());
        if(config != null) {
          deployment = mapper.readValue(item.getMetadata().getAnnotations().get("kubectl.kubernetes.io/last-applied-configuration"), com.consort.kubernetesadapter.restmodel.kubernetes.Deployment.class);
          if(deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getEnv() != null)
            result = new Deployment(item.getMetadata().getName(), image[image.length-1], cond.getStatus(), item.getStatus().getReplicas()==null?0:item.getStatus().getReplicas(), cond.getReason(), deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getEnv());
        }

        return getMapper().writeValueAsString(result);
      } catch (KubernetesException e) {
        response.status(e.getStatus());
        return getMapper().writeValueAsString(e);
      }
    });
  }
  private void handleServices(final Service http) {
    try {
      http.before(DEPLOYMENTS_PATH, new AuthorizationFilter(AUTHORIZER_NAME, ROLE_ADMIN));
    } catch(KubernetesException e) {
      logger.error("Error on initializing. AuthorizationFilter not working properly.", e);
      System.exit(e.getStatus());
    }
    http.get(DEPLOYMENTS_PATH, (request, response) -> {
      final String namespace = request.params(NAMESPACE_PARAM);
      final String[] filter = (request.queryParams(FILTERBY_OPT_PARAM) != null?request.queryParams(FILTERBY_OPT_PARAM).split(","):new String[0]);
      List<com.consort.kubernetesadapter.restmodel.Deployment> result = new LinkedList<>();
      try {
        List<io.fabric8.kubernetes.api.model.extensions.Deployment> list = KubernetesAPIService.getInstance().getDeployments(namespace);
        Iterator<io.fabric8.kubernetes.api.model.extensions.Deployment> it = list.iterator();
        io.fabric8.kubernetes.api.model.extensions.Deployment item;

        while(it.hasNext()) {
          item = it.next();

          String[] image = {"NA", "NA"};
          DeploymentState cond = this.selectDeploymentCondition(item, item.getStatus().getReplicas()==null?0:item.getStatus().getReplicas());
          com.consort.kubernetesadapter.restmodel.Deployment depl;
          if(filter.length==0 || Arrays.asList(filter).contains(item.getMetadata().getName())) {
            image = item.getSpec().getTemplate().getSpec().getContainers().get(0).getImage().split(":");

            String config = item.getMetadata().getAnnotations().get("kubectl.kubernetes.io/last-applied-configuration");
            ObjectMapper mapper = new ObjectMapper();
            com.consort.kubernetesadapter.restmodel.kubernetes.Deployment deployment;
            depl = new Deployment(item.getMetadata().getName(), image[image.length - 1], cond.getStatus(), item.getStatus().getReplicas(), cond.getReason(), new LinkedList<>());
            if(config != null) {
              deployment = mapper.readValue(item.getMetadata().getAnnotations().get("kubectl.kubernetes.io/last-applied-configuration"), com.consort.kubernetesadapter.restmodel.kubernetes.Deployment.class);
              if(deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getEnv() != null)
                depl = new Deployment(item.getMetadata().getName(), image[image.length - 1], cond.getStatus(), item.getStatus().getReplicas(), cond.getReason(), deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getEnv());
            }

            result.add(depl);
          }
        }

        return getMapper().writeValueAsString(result);
      } catch (KubernetesException e) {
        response.status(e.getStatus());
        return getMapper().writeValueAsString(e);
      }
    });
  }
  private void handlePods(final Service http) {
    try {
      http.before(PODS_PATH, new AuthorizationFilter(AUTHORIZER_NAME, ROLE_ADMIN));
    } catch(KubernetesException e) {
      logger.error("Error on initializing. AuthorizationFilter not working properly.", e);
      System.exit(e.getStatus());
    }
    http.get(PODS_PATH, (request, response) -> {
      final String namespace = request.params(NAMESPACE_PARAM);
      final String service = request.params(SERVICE_PARAM);
      List<com.consort.kubernetesadapter.restmodel.Pod> result = new LinkedList<>();
      try {
        io.fabric8.kubernetes.api.model.Service item = KubernetesAPIService.getInstance().getService(namespace, service);

        List<Pod> pods = KubernetesAPIService.getInstance().getPodsByService(namespace, item);
        String[] image = {"NA", "NA"};
        Iterator<io.fabric8.kubernetes.api.model.Pod> itPod = pods.iterator();
        io.fabric8.kubernetes.api.model.Pod pod;

        while(itPod.hasNext()) {
          pod = itPod.next();

          image = pod.getSpec().getContainers().get(0).getImage().split(":");
          result.add(new com.consort.kubernetesadapter.restmodel.Pod(item.getMetadata().getName(), image[image.length-1], pods.get(0).getStatus().getPhase()));
        }

        return getMapper().writeValueAsString(result);
      } catch (KubernetesException e) {
        response.status(e.getStatus());
        return getMapper().writeValueAsString(e);
      }
    });
  }

  private DeploymentState selectDeploymentCondition(io.fabric8.kubernetes.api.model.extensions.Deployment depl, Integer instancesRunning) {
    DeploymentCondition available = null;
    DeploymentCondition progressing = null;
    DeploymentCondition failure = null;
    DeploymentState status = null;

    Iterator<DeploymentCondition> statIt = depl.getStatus().getConditions().iterator();

    while(statIt.hasNext()) {
      DeploymentCondition stat = statIt.next();

      if (stat.getType().equals("Available"))
        available = stat;
      else if (stat.getType().equals("Progressing"))
        progressing = stat;
      else if (stat.getType().equals("ReplicaFailure"))
        failure = stat;
    }

    if(failure != null && failure.getStatus().equals("True"))
      status = new DeploymentState(States.BROKEN, failure.getReason()); //Broken
    else if(progressing != null && progressing.getStatus().equals("True") && progressing.getReason().equals("NewReplicaSetAvailable"))
      status = new DeploymentState(States.RUNNING, progressing.getReason()); //Running
    else if(progressing != null && progressing.getStatus().equals("True"))
      status = new DeploymentState(States.INITIALIZING, progressing.getReason()); //Initializing
    else if(progressing != null && progressing.getStatus().equals("Unknown"))
      status = new DeploymentState(States.STOPPED, progressing.getReason()); //Stopped
    else if(progressing != null)
      status = new DeploymentState(States.BROKEN, progressing.getReason()); //Broken
    else if(available != null && available.getStatus().equals("True") && instancesRunning > 0)
      status = new DeploymentState(States.RUNNING, available.getReason()); //Running
    else if(available != null)
      status = new DeploymentState(States.STOPPED, available.getReason()); //Stopped
    else
      status = new DeploymentState(States.BROKEN, "UNDEFINED");

    return status;
  }

  private ObjectMapper getMapper() {
      return new ObjectMapper();
  }
}
