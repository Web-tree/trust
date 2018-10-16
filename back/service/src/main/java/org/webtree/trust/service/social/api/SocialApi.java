package org.webtree.trust.service.social.api;

import org.webtree.trust.domain.SocialUser;

public interface SocialApi<T extends SocialUser> {
    T getUser(String token);
}
