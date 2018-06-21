package org.webtree.trust.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.webtree.trust.AbstractSpringTest;

@AutoConfigureMockMvc
public abstract class AbstractControllerTest extends AbstractSpringTest {

    protected ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

}
