package org.webtree.trust.service.social;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.service.exception.ProviderNotSupportedException;
import org.webtree.trust.service.security.JwtTokenService;
import org.webtree.trust.util.ObjectBuilderHelper;

/**
 * Created by Udjin on 06.08.2018.
 */
@ExtendWith(MockitoExtension.class)
class SocialServiceFacadeTest {

    private final static String TRUST_USER_ID = "id";
    private final static String PROVIDER_ID = "provider_id";
    private final static String TOKEN = "token";

    @Mock
    private SocialServicesProvider provider;
    @Mock
    private FacebookService facebookService;
    @Mock
    private JwtTokenService jwtTokenService;

    private SocialServiceFacade service;
    private TrustUser trustUserWithId;
    private SocialConnectionInfo info;
    private ObjectBuilderHelper helper = new ObjectBuilderHelper();

    @BeforeEach
    void setUp() {
        service = new SocialServiceFacade(provider, jwtTokenService);
        info = SocialConnectionInfo.builder().token("321").provider(PROVIDER_ID).build();
        trustUserWithId = helper.buildNewUserWithId();
        given(provider.getService(PROVIDER_ID)).willReturn(facebookService);
    }


    @Test
    void shouldCallSocialUserServiceIfProviderSupported() {
        service.addSocialConnection(info, TRUST_USER_ID);
        verify(facebookService).addSocialConnection(info, TRUST_USER_ID);
    }

    @Test
    void shouldNotCallSocialUserServiceIfProviderNotSupported() {
        given(provider.getService(PROVIDER_ID)).willThrow(new ProviderNotSupportedException(""));

        assertThatThrownBy(() -> service.addSocialConnection(info, TRUST_USER_ID))
                .isInstanceOf(ProviderNotSupportedException.class);
    }

    @Test
    void shouldReturnTrustUserFromDBIfItExists() {
        given(facebookService.login(info)).willReturn(trustUserWithId);
        given(jwtTokenService.generateToken(trustUserWithId)).willReturn(TOKEN);
        assertThat(service.login(info)).isEqualTo(TOKEN);
    }

}