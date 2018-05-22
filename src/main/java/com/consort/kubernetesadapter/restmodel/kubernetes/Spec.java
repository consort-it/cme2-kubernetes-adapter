package com.consort.kubernetesadapter.restmodel.kubernetes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Spec {
    private int replicas;
    private List<Container> containers;
    private Template template;

    public int getReplicas() {
        return replicas;
    }

    public Template getTemplate() {
        return template;
    }

    public List<Container> getContainers() {
        return containers;
    }
}
