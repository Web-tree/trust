package org.webtree.trust.controller;

import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import org.webtree.trust.domain.AuthDetals;
import org.webtree.trust.util.ObjectBuilderHelper;
import org.webtree.trust.domain.User;
import org.webtree.trust.service.UserService;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private  PasswordEncoder passwordEncoder;


    private ObjectBuilderHelper builderHelper = new ObjectBuilderHelper();

    @Test
    public void shouldReturn2xxOkIfUserDoesntExist() throws Exception {
        AuthDetals authDetals = builderHelper.buildUserDTO();
        given(passwordEncoder.encode(authDetals.getPassword())).willReturn("someEncodedPassword");
        User user = getUserFromUserDTO(authDetals);
        given(service.saveIfNotExists(user)).willReturn(true);

        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetals)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void shouldReturn4xxIfUserAlreadyExists() throws Exception {
        AuthDetals authDetals = builderHelper.buildUserDTO();
        given(passwordEncoder.encode(authDetals.getPassword())).willReturn("12345");
        User user = getUserFromUserDTO(authDetals);
        given(service.saveIfNotExists(user)).willReturn(false);

        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDetals)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andReturn();
    }

    private User getUserFromUserDTO(AuthDetals authDetals) {
        return modelMapper.map(authDetals, User.class).enable();
    }
}