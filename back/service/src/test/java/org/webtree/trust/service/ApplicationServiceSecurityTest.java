package org.webtree.trust.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.service.security.ApplicationServicePreAuthorization;
import org.webtree.trust.service.security.WithMockCustomUser;

/**
 * Created by Udjin Skobelev on 05.12.2018.
 */
class ApplicationServiceSecurityTest extends AbstractSpringTest {
    private final static String TRUST_USER_ID = "superUserId";
    private final static String ANOTHER_TRUST_USER_ID = "newID";
    private final static String APP_ID = "1a2s3d4f5g";
    private final static String NEW_NAME = "ozzy";

    @MockBean
    private ApplicationServicePreAuthorization authorization;

    @Autowired
    private ApplicationService service;
    private TrustUser trustUser;
    private TrustUser anotherUser;

    @BeforeEach
    void setUp() {
        trustUser = TrustUser.builder().id(TRUST_USER_ID).username("user1").build();
        anotherUser = TrustUser.builder().id(ANOTHER_TRUST_USER_ID).username("user1").build();
    }

    @Test
    @WithMockCustomUser(userId = TRUST_USER_ID)
    void shouldDeleteApplicationWhenPassedCorrectCredentials() {
        given(authorization.isOwner(trustUser, APP_ID)).willReturn(true);
        service.delete(APP_ID);
        verify(applicationRepository).deleteById(APP_ID);
    }

    @Test
    @WithMockCustomUser(userId = ANOTHER_TRUST_USER_ID)
    void shouldNotDeleteApplicationWhenIncorrectCredentialsPassed() {
        given(authorization.isOwner(anotherUser,APP_ID)).willReturn(false);
        assertThatThrownBy(() -> service.delete(APP_ID)).isInstanceOf(AccessDeniedException.class);
        verify(authorization).isOwner(anotherUser,APP_ID);
    }

    @Test
    @WithMockCustomUser(userId = TRUST_USER_ID)
    void shouldUpdateApplicationWhenPassedCorrectCredentials() {
        given(authorization.isOwner(trustUser,APP_ID)).willReturn(true);
        service.update(APP_ID, NEW_NAME);
        verify(applicationRepository).updateName(APP_ID, NEW_NAME);
    }

    @Test
    @WithMockCustomUser(userId = ANOTHER_TRUST_USER_ID)
    void shouldNotUpdateApplicationWhenIncorrectCredentialsPassed() {
        given(authorization.isOwner(anotherUser,APP_ID)).willReturn(false);
        assertThatThrownBy(() -> service.update(APP_ID, NEW_NAME)).isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @WithMockCustomUser(userId = TRUST_USER_ID)
    void shouldChangeSecretOfApplicationWhenPassedCorrectCredentials() {
        given(authorization.isOwner(trustUser,APP_ID)).willReturn(true);
        service.resetSecretTo(APP_ID);
        verify(applicationRepository).updateSecret(anyString(), anyString());
    }

    @Test
    @WithMockCustomUser(userId = ANOTHER_TRUST_USER_ID)
    void shouldNotChangeSecretOfWhenIncorrectCredentialsPassed() {
        given(authorization.isOwner(anotherUser,APP_ID)).willReturn(false);
        assertThatThrownBy(() -> service.resetSecretTo(APP_ID)).isInstanceOf(AccessDeniedException.class);
    }
}