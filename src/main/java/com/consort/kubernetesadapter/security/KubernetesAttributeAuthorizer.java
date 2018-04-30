package com.consort.kubernetesadapter.security;

import org.pac4j.core.authorization.authorizer.RequireAnyAttributeAuthorizer;

public class KubernetesAttributeAuthorizer extends RequireAnyAttributeAuthorizer {
  public KubernetesAttributeAuthorizer(final String attribute, final String valueToMatch) {
    super(valueToMatch);
    setElements(attribute);
  }
}
