package org.webtree.trust.service.social;

import com.datastax.driver.core.utils.UUIDs;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.domain.SocialUser;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.exception.ProfileAlreadyLinkedWithAnotherAccountException;
import org.webtree.trust.exception.ProfileAlreadyLinkedWithCurrentAccountException;
import org.webtree.trust.exception.UnexpectedSocialRepositoryResponseException;
import org.webtree.trust.service.IdService;
import org.webtree.trust.service.TrustUserService;
import org.webtree.trust.social.SocialApi;
import org.webtree.trust.social.SocialRepository;

import java.util.Optional;

public abstract class AbstractSocialService<T extends SocialUser> implements SocialService {

    private IdService idService;
    private TrustUserService trustUserService;

    @Autowired
    public AbstractSocialService(IdService idService, TrustUserService trustUserService) {
        this.idService = idService;
        this.trustUserService = trustUserService;
    }

    private void saveUserData(T userData, String userId) {
        if (getRepository().saveIfNotExists(userData)) {
            return;
        }

        Optional<T> user = getRepository().findById(userData.getId());
        if (user.isPresent()) {
            if (userId.equals(user.get().getTrustUserId())) {
                throw new ProfileAlreadyLinkedWithCurrentAccountException();
            } else if (user.get().getTrustUserId() != null && !userId.equals(user.get().getTrustUserId())) {
                throw new ProfileAlreadyLinkedWithAnotherAccountException();
            } else {
                getRepository().save(userData);
            }
        } else {
            throw new UnexpectedSocialRepositoryResponseException();
        }
    }

    public void addSocialConnection(SocialConnectionInfo info, String id) {
        T user = getServiceApi().getUser(info.getToken());
        user.setTrustUserId(id);
        saveUserData(user, id);
    }

    @Override
    public TrustUser login(SocialConnectionInfo info) {
        T socialUser = getServiceApi().getUser(info.getToken());

        String trustUserId =
                getRepository().findById(socialUser.getId())
                        .map(SocialUser::getTrustUserId)
                        .orElseGet(() -> {
                                    String generatedId = idService.generateId();
                                    socialUser.setTrustUserId(generatedId);
                                    getRepository().save(socialUser);
                                    return generatedId;
                                }
                        );

        return trustUserService
                .findById(trustUserId).orElseGet(() -> save(generateUsernameOf(socialUser), trustUserId));
    }

    abstract SocialRepository<T> getRepository();

    abstract SocialApi<T> getServiceApi();

    abstract String generateUsernameOf(T user);

    private TrustUser save(String username, String trustUserId) {
        TrustUser user =
                TrustUser.builder().username(username).id(trustUserId).password(UUIDs.random().toString()).build();
        int addedNumber = 1;
        while (!trustUserService.saveIfNotExists(user)) {
            user.setUsername(username + "_" + addedNumber);
            addedNumber++;
        }
        return user;
    }
}
