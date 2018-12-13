package org.webtree.trust.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.webtree.trust.domain.Application;
import org.webtree.trust.service.security.CombinedPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


/**
 * Created by Udjin Skobelev on 04.12.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class ApplicationFactoryTest {
    @Mock private IdService idService;
    @Mock private CombinedPasswordEncoder encoder;
    private ApplicationFactory factory;

    @Before
    public void setUp() throws Exception {
        factory = new ApplicationFactory(idService, encoder);
    }

    @Test
    public void shouldCreateApp() {
        String id = "qwerty";
        String name = "star";
        String secret = "BigSecret";
        String trustUserId = "bigId";
        String secretBeforeEncoding = "notSecret";

        Application app = Application.Builder
            .create()
            .trustUserId(trustUserId)
            .id(id)
            .clientSecret(secret)
            .name(name)
            .build();

        given(idService.generateId()).willReturn(id);
        given(encoder.encode(secretBeforeEncoding)).willReturn(secret);

        assertThat(factory.create(name, trustUserId, secretBeforeEncoding)).isEqualTo(app);
    }
}