package org.webtree.trust.service.social;

import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.domain.TrustUser;

public interface SocialService {
    void addSocialConnection(SocialConnectionInfo info, String userId);

    TrustUser login(SocialConnectionInfo info);
}
