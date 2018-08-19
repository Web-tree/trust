package org.webtree.trust.controller;

import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.trust.domain.AuthDetails;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.util.ObjectBuilderHelper;
import org.webtree.trust.service.TrustUserService;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TrustUserControllerTest extends AbstractControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private ModelMapper modelMapper;
    @MockBean private TrustUserService service;
    private TrustUser user;
    private TrustUser userFromMapper;
    private AuthDetails authDetails;

    private ObjectBuilderHelper builderHelper = new ObjectBuilderHelper();

    @Override
    @Before
    public void setUp() {
        super.setUp();
        authDetails = builderHelper.buildAuthDetails();
        userFromMapper = builderHelper.buildNewUser();
        user = builderHelper.buildNewUser();
        given(modelMapper.map(authDetails, TrustUser.class)).willReturn(userFromMapper);
        given(service.createUser(userFromMapper)).willReturn(user);
    }

    @Test
    public void shouldReturn2XxOkIfUserDoesNotExist() throws Exception {
        given(service.saveIfNotExists(user)).willReturn(true);

        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isCreated());
        }

    @Test
    public void shouldReturn4xxIfUserAlreadyExists() throws Exception {
        given(service.saveIfNotExists(user)).willReturn(false);

        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetails)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").doesNotExist());
        }

}