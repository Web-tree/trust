package org.webtree.trust.service.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

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
