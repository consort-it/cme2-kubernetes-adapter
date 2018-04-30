package com.consort.kubernetesadapter.utils;

public enum EnvVars {
    KUBE_CLIENT_CERT("security.kube_client_cert"),
    KUBE_CLIENT_KEY("security.kube_client_key");

    private String key;

    EnvVars(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
