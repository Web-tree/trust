package org.webtree.trust.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.exception.ProviderNotSupportedException;
import org.webtree.trust.security.WithMockCustomUser;
import org.webtree.trust.service.SocialService;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockCustomUser
public class SocialControllerTest extends AbstractControllerTest {
    private static final String PROVIDER = "facebook";
    private static final String TOKEN = "token";
    private static final String TRUST_USER_ID = "trustUserId";

    @Autowired private MockMvc mockMvc;
    @MockBean private SocialService service;
    private SocialConnectionInfo info;

    @Override
    @Before
    public void setUp()  {
        super.setUp();
        info  = SocialConnectionInfo.builder().provider(PROVIDER).token(TOKEN).build();
    }
    @Test
    public void shouldReturn4xxIfProviderNotSupported() throws Exception {
        doThrow(ProviderNotSupportedException.class).when(service).addSocialConnection(info,TRUST_USER_ID);
        mockMvc
                .perform(post("/rest/social/add")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(info)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturn200CodeIfProviderExists() throws Exception {
        doNothing().when(service).addSocialConnection(info, TRUST_USER_ID);
        mockMvc
                .perform(post("/rest/social/add")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(info)))
                .andExpect(status().isCreated());
    }
}