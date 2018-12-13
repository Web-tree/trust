package org.webtree.trust.service.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.webtree.trust.data.repository.ApplicationRepository;
import org.webtree.trust.domain.Application;
import org.webtree.trust.domain.TrustUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Created by Udjin Skobelev on 06.12.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationServicePreAuthorizationTest {
    private final static String TRUST_USER_ID = "superUserId";
    private final static String APP_ID = "1a2s3d4f5g";

    @Mock
    private ApplicationRepository applicationRepository;
    private ApplicationServicePreAuthorization preAuthorization;
    private TrustUser trustUser;
    private Application application;

    @Before
    public void setUp() throws Exception {
        preAuthorization = new ApplicationServicePreAuthorization(applicationRepository);
        trustUser = TrustUser.builder().id(TRUST_USER_ID).build();
    }

    @Test
    public void shouldReturnTrueIfCurrentUserIsCreatorOfApp() {
        application = Application.Builder.create().trustUserId(TRUST_USER_ID).id(APP_ID).build();
        given(applicationRepository.findById(APP_ID)).willReturn(Optional.of(application));
        assertThat(preAuthorization.isOwner(trustUser, APP_ID)).isTrue();
    }

    @Test
    public void shouldReturnFalseIfCurrentUserIsNotCreatorOfApp() {
        application = Application.Builder.create().trustUserId("anotherId").id(APP_ID).build();
        given(applicationRepository.findById(APP_ID)).willReturn(Optional.of(application));
        assertThat(preAuthorization.isOwner(trustUser, APP_ID)).isFalse();
    }
}