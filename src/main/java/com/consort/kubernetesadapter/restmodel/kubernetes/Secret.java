package com.consort.kubernetesadapter.restmodel.kubernetes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Secret {
    private String key;
    private String name;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
