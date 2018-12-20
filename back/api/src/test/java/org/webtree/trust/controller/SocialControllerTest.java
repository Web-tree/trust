package org.webtree.trust.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.social.InvalidAuthorizationException;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.security.WithMockCustomUser;
import org.webtree.trust.service.exception.ProviderNotSupportedException;

import java.util.Locale;

@WithMockCustomUser
class SocialControllerTest extends AbstractControllerTest {
    private static final String PROVIDER = "unrealProvider";
    private static final String TOKEN = "t1o2k3n4";
    private static final String TRUST_USER_ID = "654321";
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MessageSource messageSource;
    private SocialConnectionInfo info;
    private String providerNotSupportedErrorMessage;
    private String invalidTokenErrorMessage;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        info = SocialConnectionInfo.builder().provider(PROVIDER).token(TOKEN).build();
        providerNotSupportedErrorMessage = messageSource.getMessage("social.provider.notSupported", null, Locale.getDefault());
        invalidTokenErrorMessage = messageSource.getMessage("social.invalidToken", null, Locale.getDefault());
    }

    @Test
    void shouldReturn4xxIfProviderNotSupported() throws Exception {
        doThrow(ProviderNotSupportedException.class).when(socialService).addSocialConnection(info, TRUST_USER_ID);
        mockMvc
                .perform(post("/rest/social/add")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(info)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(providerNotSupportedErrorMessage));
    }

    @Test
    void shouldReturn4xxIfTokenIsInvalid() throws Exception {
        doThrow(InvalidAuthorizationException.class).when(socialService).addSocialConnection(info, TRUST_USER_ID);
        mockMvc
                .perform(post("/rest/social/add")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(info)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(invalidTokenErrorMessage));
    }

    @Test
    void shouldReturn200CodeIfProviderExists() throws Exception {
        doNothing().when(socialService).addSocialConnection(info, TRUST_USER_ID);
        mockMvc
                .perform(post("/rest/social/add")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(info)))
                .andExpect(status().isCreated());
    }

    @WithAnonymousUser
    @Test
    void shouldReturnToken() throws Exception {
        given(socialService.login(info)).willReturn(TOKEN);

        mockMvc
                .perform(post("/rest/social/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(info)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").value(TOKEN));
    }

    @WithAnonymousUser
    @Test
    void shouldReturnBadRequestWhenTokenIsNull() throws Exception {
        SocialConnectionInfo emptyInfo = SocialConnectionInfo.builder().token(null).provider(PROVIDER).build();

        mockMvc
                .perform(post("/rest/social/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emptyInfo)))
                .andExpect(status().isBadRequest());
    }

    @WithAnonymousUser
    @Test
    void shouldReturnBadRequestWhenTokenIsEmpty() throws Exception {
        SocialConnectionInfo emptyInfo = SocialConnectionInfo.builder().token("").provider(PROVIDER).build();

        mockMvc
                .perform(post("/rest/social/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emptyInfo)))
                .andExpect(status().isBadRequest());
    }

    @WithAnonymousUser
    @Test
    void shouldReturnBadRequestWhenProviderIsNull() throws Exception {
        SocialConnectionInfo emptyInfo = SocialConnectionInfo.builder().token(TOKEN).provider(null).build();

        mockMvc
                .perform(post("/rest/social/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emptyInfo)))
                .andExpect(status().isBadRequest());
    }

    @WithAnonymousUser
    @Test
    void shouldReturnBadRequestWhenProviderIsEmpty() throws Exception {
        SocialConnectionInfo emptyInfo = SocialConnectionInfo.builder().token(TOKEN).provider("").build();

        mockMvc
                .perform(post("/rest/social/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(emptyInfo)))
                .andExpect(status().isBadRequest());
    }

    @WithAnonymousUser
    @Test
    void shouldReturnBadRequestIfProverNotSupported() throws Exception {
        doThrow(ProviderNotSupportedException.class).when(socialService).login(info);

        mockMvc
                .perform(post("/rest/social/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(info)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(providerNotSupportedErrorMessage));
    }

    @WithAnonymousUser
    @Test
    void shouldReturnBadRequestIfTokenIsInvalid() throws Exception {
        doThrow(InvalidAuthorizationException.class).when(socialService).login(info);

        mockMvc
                .perform(post("/rest/social/login")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(info)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(invalidTokenErrorMessage));
    }
}