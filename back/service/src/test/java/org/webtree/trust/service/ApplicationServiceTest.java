package org.webtree.trust.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webtree.trust.data.repository.ApplicationRepository;
import org.webtree.trust.domain.Application;
import org.webtree.trust.dto.JustCreatedApplication;
import org.webtree.trust.service.security.CombinedPasswordEncoder;

import java.util.Collections;
import java.util.List;

/**
 * Created by Udjin Skobelev on 07.11.2018.
 */

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    private static final String TRUST_USER_ID = "654321123456";
    private static final String APP_ID = "someAppID";
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

    @BeforeEach
    void setUp() {
        service = new ApplicationService(repo, encoder, factory);
        app = Application.Builder.create()
            .name(NAME)
            .clientSecret(ENCODED_SECRET)
            .trustUserId(TRUST_USER_ID)
            .id(APP_ID)
            .build();
    }

    @Test
    void shouldReturnApplicationList() {
        List<Application> applications = Collections.singletonList(app);
        given(repo.findAllByTrustUserId(TRUST_USER_ID)).willReturn(applications);
        assertThat(service.findAllByTrustUserId(TRUST_USER_ID)).isEqualTo(applications);
    }

    @Test
    void shouldCreateApplication() {
        given(factory.create(anyString(), anyString(), anyString())).willReturn(app);

        JustCreatedApplication wrapper = service.create(NAME, TRUST_USER_ID);

        assertThat(wrapper.getSecret()).isNotNull();
        assertThat(wrapper.getApplication().getName()).isEqualTo(NAME);
        assertThat(wrapper.getApplication().getTrustUserId()).isEqualTo(TRUST_USER_ID);
        assertThat(wrapper.getApplication().getId()).isEqualTo(APP_ID);
    }

    @Test
    void shouldResetSecret() {
        given(encoder.encode(anyString())).willReturn(ENCODED_SECRET);
        service.resetSecretTo(APP_ID);
        verify(repo).updateSecret(APP_ID, ENCODED_SECRET);
    }

    @Test
    void shouldReturnListOfApplications() {
        service.findAllByTrustUserId(TRUST_USER_ID);
        verify(repo).findAllByTrustUserId(TRUST_USER_ID);
    }

    @Test
    void shouldUpdateAppName() {
        String newName = "asdfghjk";
        service.update(APP_ID, newName);
        verify(repo).updateName(APP_ID, newName);
    }

    @Test
    void shouldDeleteApp() {
        service.delete(APP_ID);
        verify(repo).deleteById(APP_ID);
    }
}