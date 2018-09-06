package org.webtree.trust.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Service;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.exception.DataSavingException;
import org.webtree.trust.exception.ProfileAlreadyLinkedWithAnotherAccountException;
import org.webtree.trust.exception.ProfileAlreadyLinkedWithCurrentAccountException;
import org.webtree.trust.repository.social.FbUserRepository;
import org.webtree.trust.social.FacebookTemplateFactory;

import java.util.Optional;


@Service
public class FbUserService implements SocialUserService {

    private ModelMapper modelMapper;
    private FbUserRepository fbRepository;
    private FacebookTemplateFactory factory;

    @Autowired
    public FbUserService(FbUserRepository userRepository, ModelMapper modelMapper,
                         FacebookTemplateFactory facebookTemplateFactory) {
        this.modelMapper = modelMapper;
        this.fbRepository = userRepository;
        this.factory = facebookTemplateFactory;
    }

    public Optional<FacebookUser> findById(String id) {
        return fbRepository.findById(id);
    }

    private void saveUserData(FacebookUser userData, String userId) {
        if (fbRepository.saveIfNotExists(userData)) {
            return;
        }
        Optional<FacebookUser> user = findById(userData.getId());
        if (user.isPresent()) {
            if (userId.equals(user.get().getTrustUserId())) {
                throw new ProfileAlreadyLinkedWithCurrentAccountException();
            } else if (user.get().getTrustUserId() != null && !userId.equals(user.get().getTrustUserId())) {
                throw new ProfileAlreadyLinkedWithAnotherAccountException();
            } else {
                fbRepository.save(userData);
            }
        } else {
            throw new DataSavingException();
        }
    }

    public void addSocialConnection(SocialConnectionInfo info, String id) {
        Facebook facebook = factory.create(info.getToken());
        User user = facebook.userOperations().getUserProfile();
        FacebookUser facebookUser = modelMapper.map(user, FacebookUser.class);
        facebookUser.setTrustUserId(id);
        saveUserData(facebookUser, id);
    }
}
