package org.webtree.trust.service.social;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webtree.trust.service.exception.ProviderNotSupportedException;

/**
 * Created by Udjin Skobelev on 13.08.2018.
 */
@ExtendWith(MockitoExtension.class)
class SocialServicesProviderTest {

    @Mock
    private FacebookService service;

    private SocialServicesProvider holder;

    @BeforeEach
    void setUp() {
        holder = new SocialServicesProvider();
    }

    @Test
    void shouldAddAndGetService() {
        String providerId = "facebook";
        holder.addService(providerId, service);
        assertThat(holder.getService(providerId)).isEqualTo(service);
    }

    @Test
    void shouldThrowExceptionIfDoesNotExist() {
        assertThatThrownBy(() ->holder.getService("someRandomName"))
                .isInstanceOf(ProviderNotSupportedException.class);
    }
}