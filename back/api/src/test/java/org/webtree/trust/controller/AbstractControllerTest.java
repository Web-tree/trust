package org.webtree.trust.controller;

import static org.springframework.context.annotation.ComponentScan.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.webtree.trust.AbstractSpringTest;
import org.webtree.trust.data.repository.TrustUserLockRepository;
import org.webtree.trust.data.repository.TrustUserRepository;
import org.webtree.trust.data.repository.social.facebook.FacebookRepository;
import org.webtree.trust.data.repository.social.facebook.PrivateFbUserRepository;
import org.webtree.trust.service.TrustUserService;
import org.webtree.trust.service.social.FacebookService;
import org.webtree.trust.service.social.SocialServiceFacade;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractSpringTest {

    @MockBean private TrustUserRepository trustUserRepository;
    @MockBean private FacebookRepository facebookRepository;
    @MockBean private PrivateFbUserRepository privateFbUserRepository;
    @MockBean private TrustUserLockRepository lockRepository;
    protected ObjectMapper objectMapper;

    @MockBean
    protected SocialServiceFacade socialService;
    @MockBean
    protected TrustUserService trustUserService;
    @MockBean
    protected FacebookService facebookService;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }
}
