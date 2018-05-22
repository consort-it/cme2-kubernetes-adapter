package com.consort.kubernetesadapter.restmodel.kubernetes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Env {
    private String name;
    private String value;

    private SecretRef valueFrom;

    public String getName() {
        return name;
    }

    public String getValue() {
        return (this.value!=null?this.value:"secret");
    }

    public SecretRef getValueFrom() { return this.valueFrom; }

    public boolean isSecret() {
        return this.value == null;
    }
}
