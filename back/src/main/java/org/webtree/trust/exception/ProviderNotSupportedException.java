package org.webtree.trust.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ProviderNotSupportedException extends RuntimeException {

    public ProviderNotSupportedException(String providerId) {
    super("No SocialAdapter for Provider [" + providerId + "] is registered"); }
}
