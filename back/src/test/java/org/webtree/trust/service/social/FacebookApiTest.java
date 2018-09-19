package org.webtree.trust.service.social;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.social.FacebookApi;
import org.webtree.trust.social.FacebookTemplateFactory;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by Udjin Skobelev on 03.09.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class FacebookApiTest {
    private FacebookApi facebookApi;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private FacebookTemplateFactory facebookFactory;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private User user;
    @Mock
    private FacebookUser fbUser;

    @Before
    public void setUp() throws Exception {
        facebookApi = new FacebookApi(facebookFactory, modelMapper);
    }

    @Test
    public void shouldReturnUser() {
        String token = "token";
        given(facebookFactory.create(token).userOperations().getUserProfile()).willReturn(user);
        given(modelMapper.map(user, FacebookUser.class)).willReturn(fbUser);
        assertThat(facebookApi.getUser(token)).isEqualTo(fbUser);
    }
}