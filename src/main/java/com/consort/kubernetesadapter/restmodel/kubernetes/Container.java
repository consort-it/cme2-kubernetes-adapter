package com.consort.kubernetesadapter.restmodel.kubernetes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Container {
    private List<Env> env;
    private String image;
    private String name;

    public List<Env> getEnv() {
        return env;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
