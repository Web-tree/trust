package org.webtree.trust.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.webtree.trust.data.repository.ApplicationRepository;
import org.webtree.trust.domain.TrustUser;

/**
 * Created by Udjin Skobelev on 03.12.2018.
 */

@Component("AppPreAuthorize")
public class ApplicationServicePreAuthorization {
    private ApplicationRepository repository;

    @Autowired
    public ApplicationServicePreAuthorization(ApplicationRepository repository) {
        this.repository = repository;
    }

    public boolean isOwner(TrustUser user, String id) {
        return repository.findById(id)
            .filter(app -> app.getTrustUserId().equals(user.getId()))
            .isPresent();
    }
}