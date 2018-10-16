package org.webtree.trust.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProviderNotSupportedException extends RuntimeException {

    public ProviderNotSupportedException(String providerId) {
        super("No SocialAdapter for Provider [" + providerId + "] is registered");
    }
}
