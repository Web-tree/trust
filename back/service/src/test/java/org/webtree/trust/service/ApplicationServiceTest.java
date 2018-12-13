package org.webtree.trust.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.webtree.trust.data.repository.ApplicationRepository;
import org.webtree.trust.domain.Application;
import org.webtree.trust.dto.JustCreatedApplication;
import org.webtree.trust.service.security.CombinedPasswordEncoder;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Created by Udjin Skobelev on 07.11.2018.
 */

@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceTest {
    private static final String TRUST_USER_ID = "654321123456";
    private static final String APP_ID = "someAppID";
    private static final String SECRET = "someAppSecret";
    private static final String NAME = "someAppName";
    private static final String ENCODED_SECRET = "zxcvbnm";


    private ApplicationService service;
    @Mock
    private ApplicationRepository repo;
    @Mock
    private CombinedPasswordEncoder encoder;
    @Mock
    private ApplicationFactory factory;
    private Application app;

    @Before
    public void setUp() throws Exception {
        service = new ApplicationService(repo, encoder, factory);
        app = Application.Builder.create()
            .name(NAME)
            .clientSecret(ENCODED_SECRET)
            .trustUserId(TRUST_USER_ID)
            .id(APP_ID)
            .build();
    }

    @Test
    public void shouldReturnApplicationList() {
        List<Application> applications = Collections.singletonList(app);
        given(repo.findAllByTrustUserId(TRUST_USER_ID)).willReturn(applications);
        assertThat(service.findAllByTrustUserId(TRUST_USER_ID)).isEqualTo(applications);
    }

    @Test
    public void shouldCreateApplication() {
        given(factory.create(anyString(), anyString(), anyString())).willReturn(app);

        JustCreatedApplication wrapper = service.create(NAME, TRUST_USER_ID);

        assertThat(wrapper.getSecret()).isNotNull();
        assertThat(wrapper.getApplication().getName()).isEqualTo(NAME);
        assertThat(wrapper.getApplication().getTrustUserId()).isEqualTo(TRUST_USER_ID);
        assertThat(wrapper.getApplication().getId()).isEqualTo(APP_ID);
    }

    @Test
    public void shouldResetSecret() {
        given(encoder.encode(anyString())).willReturn(ENCODED_SECRET);
        String newSecret = service.resetSecretTo(APP_ID);
        verify(repo).updateSecret(APP_ID, ENCODED_SECRET);
    }

    @Test
    public void shouldReturnListOfApplications() {
        service.findAllByTrustUserId(TRUST_USER_ID);
        verify(repo).findAllByTrustUserId(TRUST_USER_ID);
    }

    @Test
    public void shouldUpdateAppName() {
        String newName = "asdfghjk";
        service.update(APP_ID, newName);
        verify(repo).updateName(APP_ID, newName);
    }

    @Test
    public void shouldDeleteApp() {
        service.delete(APP_ID);
        verify(repo).deleteById(APP_ID);
    }
}