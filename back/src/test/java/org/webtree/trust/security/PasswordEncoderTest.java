package org.webtree.trust.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Udjin Skobelev on 22.09.2018.
 */

public class PasswordEncoderTest {

    private final static String PASSWORD_TO_ENCODE = "usualPassword";
    private PasswordEncoder encoder;

    @Before
    public void setUp() {
        encoder = new CombinedPasswordEncoder("longPassword", "9e0b5328c644e94a");
    }

    @Test
    public void shouldEncodePassword() {
        assertThat(encoder.encode(PASSWORD_TO_ENCODE)).isNotEmpty();
    }

    @Test
    public void shouldReturnTrueIfPasswordMatches() {
        String encodedString = encoder.encode(PASSWORD_TO_ENCODE);
        assertThat(encoder.matches(PASSWORD_TO_ENCODE, encodedString)).isTrue();
    }

    @Test
    public void shouldReturnFalseIfPasswordDoNotMatches() {
        String encodedString = encoder.encode(PASSWORD_TO_ENCODE);
        assertThat(encoder.matches("newPassword", encodedString)).isFalse();
    }
}
