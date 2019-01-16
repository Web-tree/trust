package org.webtree.trust.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.MockMvc;
import org.webtree.trust.domain.Application;
import org.webtree.trust.dto.JustCreatedApplication;
import org.webtree.trust.security.WithMockCustomUser;
import org.webtree.trust.service.ApplicationService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Udjin Skobelev on 02.11.2018.
 */

@WithMockCustomUser
class ApplicationControllerTest extends AbstractControllerTest {
    private static final String TRUST_USER_ID = "654321";
    private static final String APP_NAME = "someName";
    private static final String APP_ID = "someId";
    private static final String SECRET = "someSecret";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ApplicationService service;
    private JustCreatedApplication wrapper;
    private Application app;

    @BeforeEach
    void setUp() {
        app = Application.Builder
            .create()
            .name(APP_NAME)
            .clientSecret(SECRET)
            .id(APP_ID)
            .build();
        wrapper = new JustCreatedApplication(SECRET, app);
    }

    @Test
    void shouldReturnListOfApplications() throws Exception {
        List<Application> list = buildApplications();
        given(service.findAllByTrustUserId(TRUST_USER_ID)).willReturn(list);
        mockMvc
            .perform(
                get("/rest/app/list")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").value(list.get(0).getId()))
            .andExpect(jsonPath("$[1].id").value(list.get(1).getId()))
            .andExpect(jsonPath("$[2].id").value(list.get(2).getId()));
    }

    private List<Application> buildApplications() {
        return Arrays.asList(
            Application.Builder.create().id("12345").trustUserId(TRUST_USER_ID).build(),
            Application.Builder.create().id("987654").trustUserId(TRUST_USER_ID).build(),
            Application.Builder.create().id("356478").trustUserId(TRUST_USER_ID).build()
        );
    }

    @Test
    void shouldCreateApp() throws Exception {
        given((service.create(APP_NAME, TRUST_USER_ID))).willReturn(wrapper);
        given(service.save(app)).willReturn(app);
        mockMvc
            .perform(
                post("/rest/app")
                    .contentType(MediaType.APPLICATION_JSON).content(APP_NAME))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.secret").value(SECRET))
            .andExpect(jsonPath("$.application.name").value(APP_NAME));
    }

    @Test
    void shouldReturnNewSecret() throws Exception {

        given(service.resetSecretTo(APP_ID)).willReturn(SECRET);

        mockMvc
            .perform(
                post("/rest/app/reset").param("id", APP_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(SECRET));
    }

    @Test
    void shouldUpdateApplicationName() throws Exception {
        String newName = "zxcvbnjhgfdsa";

        mockMvc
            .perform(
                put("/rest/app").content(newName).param("id", APP_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        verify(service).update(APP_ID, newName);
    }

    @Test
    void shouldDeleteApplication() throws Exception {
        mockMvc
            .perform(delete("/rest/app").param("id", APP_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(service).delete(APP_ID);
    }

    @Test
    void shouldReturnForbiddenIfWrongCredentialsPassed() throws Exception {
        doThrow(AccessDeniedException.class).when(service).delete(APP_ID);
        mockMvc
            .perform(delete("/rest/app").param("id", APP_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());

    }
}