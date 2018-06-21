package org.webtree.trust.controller;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.trust.util.UserHelper;
import org.webtree.trust.domain.User;
import org.webtree.trust.service.UserService;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    private UserHelper userHelperService = new UserHelper();

    @Test
    public void shouldReturn2xxOkIfUserDoesntExist() throws Exception {
        User user = userHelperService.buildUser();
        given(service.saveIfNotExists(user)).willReturn(true);
        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void shouldReturn4xxIfUserAlreadyExists() throws Exception {
        User user = userHelperService.buildUser();
        given(service.saveIfNotExists(user)).willReturn(false);
        mockMvc
                .perform(post("/rest/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").exists())
                .andReturn();
    }
}