package com.consort.kubernetesadapter.security;

import com.consort.kubernetesadapter.exception.KubernetesException;
import com.consort.kubernetesadapter.restmodel.Errors;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pac4j.core.http.HttpActionAdapter;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class SecurityHttpActionAdapter extends DefaultHttpActionAdapter {
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  public SecurityHttpActionAdapter() {
  }

  @Override
  public Object adapt(int code, SparkWebContext context) {
    this.logger.debug("requires HTTP action: {}", code);
    ObjectMapper map = new ObjectMapper();
    try {
      if (code == 401) {
        Spark.halt(401, map.writeValueAsString(new KubernetesException(Errors.ERR_AUTHENTICATION_REQUIRED, "SecurityHttpActionAdapter")));
      } else if (code == 403) {
        Spark.halt(403, map.writeValueAsString(new KubernetesException(Errors.ERR_FORBIDDEN, "SecurityHttpActionAdapter")));
      } else if (code == 404) {
        Spark.halt(404, map.writeValueAsString(new KubernetesException(Errors.ERR_PAGE_NOT_FOUND, "SecurityHttpActionAdapter")));
      } else if (code == 200) {
        Spark.halt(200, context.getSparkResponse().body());
      } else if (code == 302) {
        context.getSparkResponse().redirect(context.getLocation());
      }
    } catch(JsonProcessingException e) {
      System.out.println(e);
    } catch(Exception e) {
      throw e;
    }
    return null;
  }
}
