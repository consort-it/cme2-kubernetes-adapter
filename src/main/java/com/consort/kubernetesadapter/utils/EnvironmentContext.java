package com.consort.kubernetesadapter.utils;

import com.consort.kubernetesadapter.exception.KubernetesException;
import com.consort.kubernetesadapter.restmodel.Errors;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.lang3.StringUtils;

public class EnvironmentContext {

    private static EnvironmentContext context = null;
    private Dotenv dotenv = null;

    private EnvironmentContext() {
        initEnvironment();
    }

    public static EnvironmentContext getInstance() {
        if (context == null) {
            context = new EnvironmentContext();
        }

        return context;
    }

    private void initEnvironment() {
        try {
            dotenv = Dotenv.configure().load();
        } catch (Exception e) {
            System.out.println("INFO: Dotenv configuration failed! Ignore if running on prod!");
        }
    }

    public String getenv(final String propertyName) throws KubernetesException {
        if (dotenv != null) {
            return dotenv.get(propertyName);
        } else {
            final String systemProperty = System.getenv(propertyName);
            if (StringUtils.isBlank(systemProperty)) {
                System.out.println("WARNING: Could not find system property -> " + systemProperty + "!");
                throw new KubernetesException(Errors.ERR_PROPERTY_NOT_FOUND, "SecurityHttpActionAdapter("+systemProperty+")");
            }
            return systemProperty;
        }
    }
}
