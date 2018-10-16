package org.webtree.trust.service.social;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.service.security.JwtTokenService;

@Service
public class SocialServiceFacade {

    private SocialServicesProvider socialServicesProvider;
    private JwtTokenService jwtTokenService;

    @Autowired
    public SocialServiceFacade(SocialServicesProvider socialServicesProvider,
                               JwtTokenService jwtTokenService) {
        this.socialServicesProvider = socialServicesProvider;
        this.jwtTokenService = jwtTokenService;
    }

    public void addSocialConnection(SocialConnectionInfo info, String trustUserId) {
        checkNotNull(info);
        SocialService socialService = socialServicesProvider.getService(info.getProvider());
        socialService.addSocialConnection(info, trustUserId);
    }

    public String login(SocialConnectionInfo info) {
        SocialService socialService = socialServicesProvider.getService(info.getProvider());
        TrustUser user = socialService.login(info);
        return jwtTokenService.generateToken(user);
    }
}
