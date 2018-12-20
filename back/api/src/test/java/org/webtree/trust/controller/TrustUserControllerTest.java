package org.webtree.trust.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.trust.domain.AuthDetails;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.util.ObjectBuilderHelper;

class TrustUserControllerTest extends AbstractControllerTest {

    private final static String USERNAME = "JOHN_SNOW";

    private static final String PASSWORD =
            "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2" +
                    "a1b2a1b2a1b2a1b2a1b2a1b2a1b2a1b2";

    private static final String NOT_SHA512_MSG = "The password must be a representation of sha512";
    private static final String USER_EXISTS_MSG = "User with this username already exists.";
    private ObjectMapper objectMapper;


    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ModelMapper modelMapper;
    private TrustUser user;
    private AuthDetails authDetails;

    private ObjectBuilderHelper builderHelper = new ObjectBuilderHelper();

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        authDetails = AuthDetails.builder().username(USERNAME).password(PASSWORD).build();
        TrustUser userFromMapper = builderHelper.buildNewUser();
        user = builderHelper.buildNewUser();
        given(modelMapper.map(authDetails, TrustUser.class)).willReturn(userFromMapper);
        given(trustUserService.createUser(userFromMapper)).willReturn(user);
    }

    @Test
    void shouldReturn2XxOkIfUserDoesNotExist() throws Exception {
        given(trustUserService.saveIfNotExists(user)).willReturn(true);

        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn4xxIfUserAlreadyExists() throws Exception {
        given(trustUserService.saveIfNotExists(user)).willReturn(false);

        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(USER_EXISTS_MSG))
                .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    void shouldReturnBadRequestIFPasswordIsNotSha512() throws Exception {
        AuthDetails details = AuthDetails.builder().password("winter_is_coming").username(USERNAME).build();

        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(details)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(NOT_SHA512_MSG));
    }
}