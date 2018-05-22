package com.consort.kubernetesadapter.restmodel.kubernetes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SecretRef {
    private Secret valueFrom;

    public Secret getValueFrom() {
        return valueFrom;
    }
}
