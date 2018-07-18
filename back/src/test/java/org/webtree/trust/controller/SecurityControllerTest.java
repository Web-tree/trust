package org.webtree.trust.controller;


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
import org.webtree.trust.domain.AuthDetals;
import org.webtree.trust.domain.User;
import org.webtree.trust.security.JwtTokenUtil;
import org.webtree.trust.service.UserService;
import org.webtree.trust.util.ObjectBuilderHelper;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityControllerTest extends AbstractControllerTest {

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASS = "testPass";

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectBuilderHelper objectBuilder = new ObjectBuilderHelper();


    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JwtTokenUtil tokenUtil;

    @Test
    public void whenLoginWithCorrectUser_shouldReturnValidToken() throws Exception {
        //AuthDetals for request with nonEncoded password
        AuthDetals authDetals = objectBuilder.buildUserDTO();

        //User that is stored in DB with already encoded password
        User user = getUserFromUserDTO(authDetals);

        given(userService.loadUserByUsername(authDetals.getUsername())).willReturn(user);
        MvcResult mvcResult = mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetals))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andReturn();
        String token = mvcResult.getResponse().getContentAsString();
        assertThat(tokenUtil.validateToken(token, user)).isTrue();
    }

    @Test
    @WithAnonymousUser
    public void whenRefreshWithInvalidToken_shouldReturnError() throws Exception {
        mockMvc.perform(get("/rest/token/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenLoginWithIncorrectUsername_shouldReturnErrorMessage() throws Exception {
        AuthDetals authDetals = objectBuilder.buildUserDTO();
        given(userService.loadUserByUsername(authDetals.getUsername())).willReturn(null);
        ResultActions actions = mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetals))
        );

        assertUnauthorized(actions);
    }

    @Test
    public void whenLoginWithIncorrectPassword_shouldReturnErrorMessage() throws Exception {
        AuthDetals authDetals = objectBuilder.buildUserDTO();

        given(userService.loadUserByUsername(authDetals.getUsername())).willReturn(getUserFromUserDTO(authDetals));
        AuthDetals wrongPasswordUser = AuthDetals.builder().username(authDetals.getUsername()).password("123").build();

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
        AuthDetals authDetals = objectBuilder.buildUserDTO();

        User userWithEncodedPassword = getUserFromUserDTO(authDetals);

        given(userService.loadUserByUsername(authDetals.getUsername())).willReturn(userWithEncodedPassword);
        MvcResult mvcResult = mockMvc.perform(
                post("/rest/token/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetals))
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



    private User getUserFromUserDTO(AuthDetals authDetals) {
        return modelMapper.map(authDetals, User.class).enable();
    }
}