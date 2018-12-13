package org.webtree.trust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.webtree.trust.domain.Application;
import org.webtree.trust.service.security.CombinedPasswordEncoder;

/**
 * Created by Udjin Skobelev on 03.12.2018.
 */

@Component
public class ApplicationFactory {

    private IdService idService;
    private CombinedPasswordEncoder encoder;

    @Autowired
    public ApplicationFactory(IdService idService, CombinedPasswordEncoder encoder) {
        this.idService = idService;
        this.encoder = encoder;
    }

    public Application create(String appName, String trustUserId, String secret) {
        return Application.Builder.create()
            .trustUserId(trustUserId)
            .id(idService.generateId())
            .clientSecret(encoder.encode(secret))
            .name(appName)
            .build();
    }
}