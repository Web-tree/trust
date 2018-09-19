package org.webtree.trust.social;

import org.webtree.trust.domain.SocialUser;

public interface SocialApi<T extends SocialUser> {
    T getUser(String token);
}
