package com.consort.kubernetesadapter.restmodel.kubernetes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Deployment {
    private String apiVersion;
    private String kind;

    private Metadata metadata;
    private Spec spec;

    public String getApiVersion() {
        return apiVersion;
    }

    public String getKind() {
        return kind;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public Spec getSpec() {
        return spec;
    }

}
