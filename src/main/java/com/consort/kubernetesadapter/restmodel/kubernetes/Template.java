package com.consort.kubernetesadapter.restmodel.kubernetes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Template {
    private Spec spec;

    public Spec getSpec() {
        return spec;
    }
}
