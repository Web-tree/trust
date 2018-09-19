package org.webtree.trust.social;

import org.webtree.trust.exception.ProviderNotSupportedException;
import org.webtree.trust.service.social.SocialService;

import java.util.HashMap;
import java.util.Map;

public class SocialServicesProvider {
    private Map<String, SocialService> services;

    public SocialServicesProvider() {
        this.services = new HashMap<>();
    }

    public void addService(String providerId, SocialService service) {
        services.put(providerId, service);
    }

    public SocialService getService(String providerId) {
        if (services.containsKey(providerId)) {
            return this.services.get(providerId);
        } else {
            throw new ProviderNotSupportedException(providerId);
        }
    }
}
