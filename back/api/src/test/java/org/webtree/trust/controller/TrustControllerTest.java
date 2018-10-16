package org.webtree.trust.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.trust.provider.Provider;
import org.webtree.trust.security.WithMockCustomUser;

@WithMockCustomUser
public class TrustControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnExpectedValueOfTrust() throws Exception {
        String userId = "someid";
        Float trustValue = 0.5F;

        mockMvc
                .perform(get("/rest/alfa/trust").param("provider",Provider.FACEBOOK.toString()).param("userId",userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").value(trustValue));
    }

    @Test
    public void shouldReturn4xxIfIncorrectProviderPasses() throws Exception {
        String provider = "someRandomProvider";
        String userId = "someUUID";
        String errorMsg = "No such provider: ";

        mockMvc
                .perform(get("/rest/alfa/trust") .param("provider",provider).param("userId",userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$").value(errorMsg + provider));
    }

    @Test
    public void shouldReturn4xxIfOneOrTwoParameterIsMissed() throws Exception {
        String provider = "someRandomProvider";
        String userId = "someUUID";

        mockMvc
                .perform(get("/rest/alfa/trust")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc
                .perform(get("/rest/alfa/trust").param("provider",provider)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        mockMvc
                .perform(get("/rest/alfa/trust").param("userId",userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        }
}