package org.webtree.trust.service.social;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.webtree.social.stackexchange.api.StackExchange;
import org.webtree.social.stackexchange.api.impl.StackExchangeTemplate;

@Component
public class StackExchangeTemplateFactory {
    @Value("${stackexchange.key}")
    private String key;

    public StackExchangeTemplateFactory() {
    }

    public StackExchange create(String token) {
        return new StackExchangeTemplate(this.key, token);
    }
}
