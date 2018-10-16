package org.webtree.trust.service.social;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.social.facebook.api.User;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.service.social.api.FacebookApi;

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