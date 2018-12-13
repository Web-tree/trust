package org.webtree.trust.dto;

import org.webtree.trust.domain.Application;

/**
 * Created by Udjin Skobelev on 03.12.2018.
 */
public class JustCreatedApplication {
    private String secret;
    private Application application;

    public JustCreatedApplication(String secret, Application application) {
        this.secret = secret;
        this.application = application;
    }

    public String getSecret() {
        return secret;
    }

    public Application getApplication() {
        return application;
    }
}