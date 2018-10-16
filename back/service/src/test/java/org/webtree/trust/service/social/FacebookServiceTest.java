package org.webtree.trust.service.social;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.webtree.trust.data.repository.social.facebook.FacebookRepository;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.service.IdService;
import org.webtree.trust.service.TrustUserService;
import org.webtree.trust.service.social.api.FacebookApi;


/**
 * Created by Udjin Skobelev on 07.08.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class FacebookServiceTest {

    private final static String NAME = "JohnDoe";
    private final static String FB_USER_ID = "1q2w3e4r5t";

    @Mock
    private FacebookRepository repo;
    @Mock
    private FacebookApi facebookApi;
    @Mock
    private IdService idService;
    @Mock
    private TrustUserService trustUserService;

    private FacebookService service;
    private FacebookUser user;

    @Before
    public void setUp() {
        service = new FacebookService(idService, trustUserService, facebookApi, repo);
        user = FacebookUser.builder().id(FB_USER_ID).name(NAME).build();
    }

    @Test
    public void shouldReturnRepository() {
        assertThat(service.getRepository()).isEqualTo(repo);
    }

    @Test
    public void shouldReturnAPi() {
        assertThat(service.getServiceApi()).isEqualTo(facebookApi);
    }

    @Test
    public void shouldReturnUsernameOfUser() {
        assertThat(service.generateUsernameOf(user)).isEqualTo(NAME);
    }

    @Test
    public void shouldChangeSpaceToUnderscoreInNameIfUserHasIt() {
        FacebookUser user = FacebookUser.builder().id(FB_USER_ID).name("John Doe").build();
        assertThat(service.generateUsernameOf(user)).isEqualTo("John_Doe");
    }
}
