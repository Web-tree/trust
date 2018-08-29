package org.webtree.trust.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.webtree.trust.AbstractSpringTest;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.exception.ProviderNotSupportedException;
import org.webtree.trust.social.SocialServicesHolder;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Created by Udjin on 06.08.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class SocialServiceTest extends AbstractSpringTest {

    @Mock
    private SocialServicesHolder holder;

    @Mock
    private FbUserService fbUserService;

    private SocialService service;

    @Before
    public void setUp() throws Exception {
        service = new SocialService(holder);
    }

    @Test
    public void shouldCallSocialUserServiceIfProviderSupported() {
        String userId = "someId";
        SocialConnectionInfo info = SocialConnectionInfo.builder().token("321").provider("facebook").build();
        given(holder.getService("facebook")).willReturn(fbUserService);
        service.addSocialConnection(info, userId);
        verify(fbUserService, Mockito.times(1)).addSocialConnection(info, userId);
    }

    @Test(expected = ProviderNotSupportedException.class)
    public void shouldNotCallSocialUserServiceIfProviderNotSupported() {
        String userId = "someId";
        SocialConnectionInfo info = SocialConnectionInfo.builder().token("321").provider("facebook").build();
        given(holder.getService("facebook")).willThrow(new ProviderNotSupportedException(""));
        service.addSocialConnection(info, userId);
    }

}