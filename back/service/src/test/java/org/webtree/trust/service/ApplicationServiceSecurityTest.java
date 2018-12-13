package org.webtree.trust.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.webtree.trust.domain.Application;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.service.security.ApplicationServicePreAuthorization;
import org.webtree.trust.service.security.WithMockCustomUser;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Created by Udjin Skobelev on 05.12.2018.
 */

public class ApplicationServiceSecurityTest extends AbstractSpringTest {
    private final static String TRUST_USER_ID = "superUserId";
    private final static String ANOTHER_TRUST_USER_ID = "newID";
    private final static String APP_ID = "1a2s3d4f5g";
    private final static String NEW_NAME = "ozzy";

    @MockBean
    private ApplicationServicePreAuthorization authorization;

    @Autowired
    private ApplicationService service;
    private Application application;
    private TrustUser trustUser;
    private TrustUser anotherUser;

    @Before
    public void setUp() throws Exception {
        application = Application.Builder.create().trustUserId(TRUST_USER_ID).id(APP_ID).build();
        trustUser = TrustUser.builder().id(TRUST_USER_ID).username("user1").build();
        anotherUser = TrustUser.builder().id(ANOTHER_TRUST_USER_ID).username("user1").build();
    }

    @Test
    @WithMockCustomUser(userId = TRUST_USER_ID)
    public void shouldDeleteApplicationWhenPassedCorrectCredentials() {
        given(authorization.isOwner(trustUser, APP_ID)).willReturn(true);
        service.delete(APP_ID);
        verify(applicationRepository).deleteById(APP_ID);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser(userId = ANOTHER_TRUST_USER_ID)
    public void shouldNotDeleteApplicationWhenIncorrectCredentialsPassed() {
        given(authorization.isOwner(anotherUser,APP_ID)).willReturn(false);
        service.delete(APP_ID);
        verify(authorization).isOwner(anotherUser,APP_ID);
    }

    @Test
    @WithMockCustomUser(userId = TRUST_USER_ID)
    public void shouldUpdateApplicationWhenPassedCorrectCredentials() {
        given(authorization.isOwner(trustUser,APP_ID)).willReturn(true);
        service.update(APP_ID, NEW_NAME);
        verify(applicationRepository).updateName(APP_ID, NEW_NAME);
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser(userId = ANOTHER_TRUST_USER_ID)
    public void shouldNotUpdateApplicationWhenIncorrectCredentialsPassed() {
        given(authorization.isOwner(anotherUser,APP_ID)).willReturn(false);
        service.update(APP_ID, NEW_NAME);
    }

    @Test
    @WithMockCustomUser(userId = TRUST_USER_ID)
    public void shouldChangeSecretOfApplicationWhenPassedCorrectCredentials() {
        given(authorization.isOwner(trustUser,APP_ID)).willReturn(true);
        service.resetSecretTo(APP_ID);
        verify(applicationRepository).updateSecret(anyString(), anyString());
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockCustomUser(userId = ANOTHER_TRUST_USER_ID)
    public void shouldNotChangeSecretOfWhenIncorrectCredentialsPassed() {
        given(authorization.isOwner(anotherUser,APP_ID)).willReturn(false);
        service.resetSecretTo(APP_ID);
    }
}