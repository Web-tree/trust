package org.webtree.trust.service.social.api;

import org.modelmapper.ModelMapper;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Component;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.service.social.FacebookTemplateFactory;

@Component
public class FacebookApi implements SocialApi {
    private FacebookTemplateFactory facebookFactory;
    private ModelMapper modelMapper;

    public FacebookApi(FacebookTemplateFactory facebookFactory, ModelMapper modelMapper) {
        this.facebookFactory = facebookFactory;
        this.modelMapper = modelMapper;
    }

    @Override
    public FacebookUser getUser(String token) {
        Facebook facebook = facebookFactory.create(token);
        User user = facebook.userOperations().getUserProfile();
        return modelMapper.map(user, FacebookUser.class);
    }
}
