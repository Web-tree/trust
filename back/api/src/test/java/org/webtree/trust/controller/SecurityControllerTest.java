package org.webtree.trust.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.webtree.trust.domain.AuthDetails;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.service.TrustUserService;
import org.webtree.trust.service.security.JwtTokenService;

import java.util.Locale;

public class SecurityControllerTest extends AbstractControllerTest {

    private static final String TEST_USERNAME = "Johnny";
    private static final String PASSWORD =
            "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2";

    private static final String WRONG_PASSWORD =
            "c1d2c1d2c1d2c1d2c1d2c1d2c1d2c1d2" +
                    "c1d2c1d2c1d2c1d2c1d2c1d2c1d2c1d2" +
                    "c1d2c1d2c1d2c1d2c1d2c1d2c1d2c1d2" +
                    "c1d2c1d2c1d2c1d2c1d2c1d2c1d2c1d2";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JwtTokenService tokenUtil;
    private AuthDetails authDetails;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        authDetails = AuthDetails.builder().username(TEST_USERNAME).password(PASSWORD).build();
    }

    @Test
    public void whenLoginWithCorrectUser_shouldReturnValidToken() throws Exception {
        //TrustUser that is stored in DB with already encoded password
        TrustUser trustUser = getUserFromUserDTO(authDetails);

        given(trustUserService.loadUserByUsername(authDetails.getUsername())).willReturn(trustUser);
        MvcResult mvcResult = mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andReturn();
        String token = mvcResult.getResponse().getContentAsString();
        assertThat(tokenUtil.validateToken(token, trustUser)).isTrue();
    }

    @Test
    @WithAnonymousUser
    public void whenRefreshWithInvalidToken_shouldReturnError() throws Exception {
        mockMvc.perform(get("/rest/token/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenLoginWithIncorrectUsername_shouldReturnErrorMessage() throws Exception {
        given(trustUserService.loadUserByUsername(authDetails.getUsername())).willReturn(null);
        ResultActions actions = mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails))
        );

        assertUnauthorized(actions);
    }

    @Test
    public void whenLoginWithNotSha512Password_shouldReturnErrorMessage() throws Exception {
        AuthDetails wrongPasswordUser = AuthDetails.builder().username(TEST_USERNAME).password("12345").build();

        mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongPasswordUser)))
                .andExpect(jsonPath("$").value("The password must be a representation of sha512"));
    }

    @Test
    public void whenLoginWithWrongPassword_shouldReturnErrorMessage() throws Exception {
        AuthDetails wrongPasswordUser = AuthDetails.builder().username(TEST_USERNAME).password(WRONG_PASSWORD).build();

        given(trustUserService.loadUserByUsername(TEST_USERNAME)).willReturn(getUserFromUserDTO(authDetails));

        ResultActions actions = mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongPasswordUser))
        );

        assertUnauthorized(actions);
    }

    @WithAnonymousUser
    @Test
    public void whenDoingRequestsWithValidTokenItShouldWork() throws Exception {
        TrustUser trustUserWithEncodedPassword = getUserFromUserDTO(authDetails);

        given(trustUserService.loadUserByUsername(authDetails.getUsername())).willReturn(trustUserWithEncodedPassword);
        MvcResult mvcResult = mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andReturn();

        String token = mvcResult.getResponse().getContentAsString();
        String authorizationHeader = "Bearer " + token;

        mockMvc
                .perform(
                        get("/rest/alfa/trust/")
                                .param("provider", "FACEBOOK")
                                .param("userId", "12345")
                                .header("Authorization", authorizationHeader))
                .andExpect(status().isOk());
    }

    private void assertUnauthorized(ResultActions resultActions) throws Exception {
        String errorMessage = messageSource.getMessage("login.badCredentials", null, Locale.getDefault());

        resultActions
                .andExpect(status().is(401))
                .andExpect(jsonPath("$").value(errorMessage));
    }


    private TrustUser getUserFromUserDTO(AuthDetails authDetals) {
        return modelMapper.map(authDetals, TrustUser.class);
    }
}