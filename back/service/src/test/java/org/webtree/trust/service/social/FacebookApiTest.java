package org.webtree.trust.service.social;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.social.facebook.api.User;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.service.social.api.FacebookApi;

/**
 * Created by Udjin Skobelev on 03.09.2018.
 */
@ExtendWith(MockitoExtension.class)
class FacebookApiTest {
    private FacebookApi facebookApi;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private FacebookTemplateFactory facebookFactory;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private User user;
    @Mock
    private FacebookUser fbUser;

    @BeforeEach
    void setUp() {
        facebookApi = new FacebookApi(facebookFactory, modelMapper);
    }

    @Test
    void shouldReturnUser() {
        String token = "token";
        given(facebookFactory.create(token).userOperations().getUserProfile()).willReturn(user);
        given(modelMapper.map(user, FacebookUser.class)).willReturn(fbUser);
        assertThat(facebookApi.getUser(token)).isEqualTo(fbUser);
    }
}