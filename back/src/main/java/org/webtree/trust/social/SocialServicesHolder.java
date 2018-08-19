package org.webtree.trust.social;

import org.webtree.trust.exception.ProviderNotSupportedException;
import org.webtree.trust.service.SocialUserService;

import java.util.HashMap;
import java.util.Map;

public class SocialServicesHolder {
    private Map<String, SocialUserService> services;

    public SocialServicesHolder() {
        this.services = new HashMap<>();
    }

    public void addService(String providerId, SocialUserService service) {
        services.put(providerId, service);
    }

    public SocialUserService getService(String providerId) {
        SocialUserService socialUserService = this.services.get(providerId);
        if (services.containsKey(providerId)) {
            return socialUserService;
        } else {
            throw new ProviderNotSupportedException(providerId);
        }
    }
}
