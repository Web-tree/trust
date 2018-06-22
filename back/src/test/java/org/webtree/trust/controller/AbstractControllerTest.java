package org.webtree.trust.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.webtree.trust.AbstractSpringTest;
import org.webtree.trust.repository.UserRepository;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractSpringTest {

    @MockBean
    private UserRepository userRepository;

    protected ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

}
