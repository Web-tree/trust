package org.webtree.trust.service.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.repository.social.FacebookRepository;
import org.webtree.trust.service.IdService;
import org.webtree.trust.service.TrustUserService;
import org.webtree.trust.social.FacebookApi;
import org.webtree.trust.social.SocialApi;
import org.webtree.trust.social.SocialRepository;

@Service
public class FacebookService extends AbstractSocialService<FacebookUser> {

    private FacebookApi facebookApi;
    private FacebookRepository repository;

    @Autowired
    public FacebookService(IdService idService,
                           TrustUserService trustUserService,
                           FacebookApi facebookApi,
                           FacebookRepository repository) {
        super(idService, trustUserService);
        this.facebookApi = facebookApi;
        this.repository = repository;
    }

    @Override
    SocialRepository getRepository() {
        return repository;
    }

    @Override
    SocialApi getServiceApi() {
        return facebookApi;
    }

    @Override
    String generateUsernameOf(FacebookUser user) {
        return user.getName().replace(" ","_");
    }

}
