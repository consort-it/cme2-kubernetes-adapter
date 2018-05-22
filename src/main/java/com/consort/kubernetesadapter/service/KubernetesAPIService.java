package com.consort.kubernetesadapter.service;

import com.consort.kubernetesadapter.exception.KubernetesException;
import com.consort.kubernetesadapter.restmodel.Errors;
import com.consort.kubernetesadapter.utils.EnvironmentContext;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.extensions.Deployment;
import io.fabric8.kubernetes.api.model.extensions.DeploymentList;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class KubernetesAPIService {

    private static KubernetesClient client;
    public static Config config;
    private static KubernetesAPIService instance;
    private static int clients = 0;

    private KubernetesAPIService() throws KubernetesException {
      final ConfigBuilder configB = new ConfigBuilder();

      configB
          .withMasterUrl(EnvironmentContext.getInstance().getenv("kubeclienturl"))
          .withUsername(EnvironmentContext.getInstance().getenv("kubeclientbeareruser"))
          .withOauthToken(EnvironmentContext.getInstance().getenv("kubeclientbearertoken"));
//              .withClientCertFile(EnvironmentContext.getInstance().getenv("kubeclientcert"))
//              .withClientKeyFile(EnvironmentContext.getInstance().getenv("kubeclientkey"));
      config = configB.build();
    }

    private static synchronized void connect() throws KubernetesException {
      try {
        if(clients++==0)
          client = new DefaultKubernetesClient(config);
      } catch(KubernetesClientException e) {
        throw new KubernetesException(Errors.ERR_CONNECTING, "Service::connect()", e);
      }
    }

    private static synchronized void disconnect() {
      if(--clients==0)
        client.close();
    }

    public static void withKubernetesReader(DefaultKubernetesClient kubernetesReader) {
      client = kubernetesReader;
    }

    public static KubernetesAPIService getInstance() throws KubernetesException {
        if (instance == null) {
            instance = new KubernetesAPIService();
        }

        return instance;
    }

    public List<Namespace> getNamespaces() throws KubernetesException {
      final NamespaceList list;
      final List<Namespace> result;
      connect();

      list = client.namespaces().list();
      result = list.getItems();

      disconnect();
      return result;
    }

    public List<Service> getServices(final String namespace) throws KubernetesException {
      final ServiceList list;
      final List<Service> result;
      if(namespace == null) throw new KubernetesException(Errors.ERR_UNDEFINED_NAMESPACE, "Service::getServices()");
      connect();

      list = client.services().inNamespace(namespace).list();
      result = list.getItems();

      disconnect();
      return result;
    }

    public Service getService(final String namespace, final String service) throws KubernetesException {
      Service result = null;
      final List<Service> list;
      if(namespace == null || namespace.isEmpty()) throw new KubernetesException(Errors.ERR_UNDEFINED_NAMESPACE, "Service::getService()");
      if(service == null || service.isEmpty()) throw new KubernetesException(Errors.ERR_UNDEFINED_SERVICE, "Service::getService()");
      connect();

      list = this.getServices(namespace);

      Iterator<Service> it = list.iterator();
      Service svc;
      while(it.hasNext()) {
        svc = it.next();
        if(svc.getMetadata().getName().equals(service)) {
          result = svc;
          break;
        }
      }

      if(result == null) throw new KubernetesException(Errors.ERR_NO_SUCH_SERVICE, "Service::getService()");

      disconnect();
      return result;
    }

    public List<Pod> getPodsByService(final String namespace, final Service service) throws KubernetesException {
      List<Pod> result = new LinkedList<>();
      final PodList list;

      if(namespace == null || namespace.isEmpty()) throw new KubernetesException(Errors.ERR_UNDEFINED_NAMESPACE, "Service::getPodsByService()");
      if(service == null) throw new KubernetesException(Errors.ERR_UNDEFINED_SERVICE, "Service::getPodsByService()");

      connect();
      list = client.pods().inNamespace(namespace).list();

      Iterator<Pod> it = list.getItems().iterator();
      Pod pod;
      while(it.hasNext()) {
        pod = it.next();
        if(pod.getMetadata().getName().contains(service.getMetadata().getName())) {
          result.add(pod);
        }
      }
      disconnect();
      return result;
    }

    public List<Deployment> getDeployments(final String namespace) throws KubernetesException {
      final DeploymentList list;

      if(namespace == null || namespace.isEmpty()) throw new KubernetesException(Errors.ERR_UNDEFINED_NAMESPACE, "Service::getDeployments()");

      connect();
      list = client.extensions().deployments().inNamespace(namespace).list();
      disconnect();
      return list.getItems();
    }

    public Deployment getDeployment(final String namespace, final String deployment) throws KubernetesException {
      Deployment result = null;
      final List<Deployment> list;
      if(namespace == null || namespace.isEmpty()) throw new KubernetesException(Errors.ERR_UNDEFINED_NAMESPACE, "Service::getDeployment()");
      if(deployment == null || deployment.isEmpty()) throw new KubernetesException(Errors.ERR_UNDEFINED_DEPLOYMENT, "Service::getDeployment()");
      connect();

      list = this.getDeployments(namespace);

      Iterator<Deployment> it = list.iterator();
      Deployment depl;
      while(it.hasNext()) {
        depl = it.next();
        if(depl.getMetadata().getName().equals(deployment)) {
          result = depl;
          break;
        }
      }

      if(result == null) throw new KubernetesException(Errors.ERR_NO_SUCH_DEPLOYMENT, "Service::getDeployment()");

      disconnect();
      return result;
    }

    public List<Pod> getPodsByDeployment(final String namespace, final Deployment deployment) throws KubernetesException {
      List<Pod> result = new LinkedList<>();
      final PodList list;

      if(namespace == null || namespace.isEmpty()) throw new KubernetesException(Errors.ERR_UNDEFINED_NAMESPACE, "Service::getPodsByDeployment()");
      if(deployment == null) throw new KubernetesException(Errors.ERR_UNDEFINED_DEPLOYMENT, "Service::getPodsByDeployment()");

      connect();
      list = client.pods().inNamespace(namespace).list();

      Iterator<Pod> it = list.getItems().iterator();
      Pod pod;
      while(it.hasNext()) {
        pod = it.next();
        if(pod.getMetadata().getName().contains(deployment.getMetadata().getName())) {
          result.add(pod);
        }
      }
      disconnect();
      return result;
    }

}
