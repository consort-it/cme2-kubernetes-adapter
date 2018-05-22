package com.consort.kubernetesadapter.restmodel.kubernetes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
    private Annotations annotations;
    private String name;
    private String namespace;

    public Annotations getAnnotations() {
        return annotations;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace;
    }
}
