package org.webtree.trust.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.webtree.trust.AbstractSpringTest;
import org.webtree.trust.repository.TrustUserLockRepository;
import org.webtree.trust.repository.social.FacebookRepository;
import org.webtree.trust.repository.TrustUserRepository;
import org.webtree.trust.repository.social.PrivateFbUserRepository;


@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractSpringTest {

    @MockBean private TrustUserRepository trustUserRepository;
    @MockBean private FacebookRepository facebookRepository;
    @MockBean private PrivateFbUserRepository privateFbUserRepository;
    @MockBean private TrustUserLockRepository lockRepository;
    protected ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

}
