package org.webtree.trust.social;

import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by Udjin Skobelev on 08.08.2018.
 */

@Component
public class FacebookTemplateFactory {
    public FacebookTemplate create(String token) {
        return new FacebookTemplate(token);
    }
}
