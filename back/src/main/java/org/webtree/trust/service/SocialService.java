package org.webtree.trust.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.social.SocialServicesHolder;


import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class SocialService {

    private SocialServicesHolder holder;

    @Autowired
    public SocialService(SocialServicesHolder holder) {
        this.holder = holder;
    }

    public void addSocialConnection(SocialConnectionInfo info, String trustUserId) {
        checkNotNull(info);
        SocialUserService userService = holder.getService(info.getProvider());
        userService.addSocialConnection(info, trustUserId);
    }
}
