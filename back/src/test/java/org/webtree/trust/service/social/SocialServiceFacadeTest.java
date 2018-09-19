package org.webtree.trust.service.social;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.webtree.trust.domain.*;
import org.webtree.trust.exception.ProviderNotSupportedException;
import org.webtree.trust.security.JwtTokenUtil;
import org.webtree.trust.social.SocialServicesProvider;
import org.webtree.trust.util.ObjectBuilderHelper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Created by Udjin on 06.08.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class SocialServiceFacadeTest {

    private final static String TRUST_USER_ID = "id";
    private final static String PROVIDER_ID = "provider_id";
    private final static String TOKEN = "token";

    @Mock
    private SocialServicesProvider provider;
    @Mock
    private FacebookService facebookService;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    private SocialServiceFacade service;
    private TrustUser trustUserWithId;
    private SocialConnectionInfo info;
    private ObjectBuilderHelper helper = new ObjectBuilderHelper();

    @Before
    public void setUp() {
        service = new SocialServiceFacade(provider, jwtTokenUtil);
        info = SocialConnectionInfo.builder().token("321").provider(PROVIDER_ID).build();
        trustUserWithId = helper.buildNewUserWithId();
        given(provider.getService(PROVIDER_ID)).willReturn(facebookService);
    }


    @Test
    public void shouldCallSocialUserServiceIfProviderSupported() {
        service.addSocialConnection(info, TRUST_USER_ID);
        verify(facebookService).addSocialConnection(info, TRUST_USER_ID);
    }

    @Test(expected = ProviderNotSupportedException.class)
    public void shouldNotCallSocialUserServiceIfProviderNotSupported() {
        given(provider.getService(PROVIDER_ID)).willThrow(new ProviderNotSupportedException(""));
        service.addSocialConnection(info, TRUST_USER_ID);
    }

    @Test
    public void shouldReturnTrustUserFromDBIfItExists() {
        given(facebookService.login(info)).willReturn(trustUserWithId);
        given(jwtTokenUtil.generateToken(trustUserWithId)).willReturn(TOKEN);
        assertThat(service.login(info)).isEqualTo(TOKEN);
    }

}