
package org.webtree.trust.service.social;

import org.springframework.stereotype.Service;
import org.webtree.trust.data.repository.social.SocialRepository;
import org.webtree.trust.data.repository.social.stackexchange.StackExchangeRepository;
import org.webtree.trust.domain.StackExchangeUser;
import org.webtree.trust.service.IdService;
import org.webtree.trust.service.TrustUserService;
import org.webtree.trust.service.social.api.SocialApi;
import org.webtree.trust.service.social.api.StackExchangeApi;

@Service
public class StackExchangeService extends AbstractSocialService<StackExchangeUser> {
    private StackExchangeApi stackExchangeApi;
    private StackExchangeRepository repo;

    public StackExchangeService(IdService idService,
                                TrustUserService trustUserService,
                                StackExchangeApi stackExchangeApi,
                                StackExchangeRepository repo) {
        super(idService, trustUserService);
        this.stackExchangeApi = stackExchangeApi;
        this.repo = repo;
    }

    SocialRepository getRepository() {
        return this.repo;
    }

    SocialApi getServiceApi() {
        return this.stackExchangeApi;
    }

    String generateUsernameOf(StackExchangeUser user) {
        return user.getDisplayName().replace(" ", "_");
    }
}
