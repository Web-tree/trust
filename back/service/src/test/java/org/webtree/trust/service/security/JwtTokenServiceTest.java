package org.webtree.trust.service.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.jsonwebtoken.ExpiredJwtException;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.service.time.TimeProvider;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stephan on 10.09.16.
 */
@ExtendWith(MockitoExtension.class)
class JwtTokenServiceTest {

    private static final String TEST_USERNAME = "testUser";
    private static final String USER_ID ="someUserId";

    @Mock
    private TimeProvider timeProviderMock;

    @InjectMocks
    private JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenService.setExpiration(3600L);
        jwtTokenService.setSecret("mySecret");
    }

    @Test
    void testGenerateTokenGeneratesDifferentTokensForDifferentCreationDates() {
        when(timeProviderMock.now())
            .thenReturn(DateUtil.yesterday())
            .thenReturn(DateUtil.now());

        final String token = createToken();
        final String laterToken = createToken();

        assertThat(token).isNotEqualTo(laterToken);
    }

    @Test
    void getUsernameFromToken() {
        when(timeProviderMock.now()).thenReturn(DateUtil.now());

        final String token = createToken();

        assertThat(jwtTokenService.getUsernameFromToken(token)).isEqualTo(TEST_USERNAME);
    }


    @Test
    void getCreatedDateFromToken() {
        final Date now = DateUtil.now();
        when(timeProviderMock.now()).thenReturn(now);

        final String token = createToken();

        assertThat(jwtTokenService.getIssuedAtDateFromToken(token)).isInSameMinuteWindowAs(now);
    }

    @Test
    void getExpirationDateFromToken() {
        final Date now = DateUtil.now();
        when(timeProviderMock.now()).thenReturn(now);
        final String token = createToken();

        final Date expirationDateFromToken = jwtTokenService.getExpirationDateFromToken(token);
        assertThat(DateUtil.timeDifference(expirationDateFromToken, now)).isCloseTo(3600000L, within(1000L));
    }

    @Test
    void expiredTokenCannotBeRefreshed() {
        when(timeProviderMock.now())
            .thenReturn(DateUtil.yesterday());
        String token = createToken();
        assertThatThrownBy(() -> jwtTokenService.canTokenBeRefreshed(token, DateUtil.tomorrow()))
                .isInstanceOf(ExpiredJwtException.class);
    }

    @Test
    void changedPasswordCannotBeRefreshed() {
        when(timeProviderMock.now())
            .thenReturn(DateUtil.now());
        String token = createToken();
        assertThat(jwtTokenService.canTokenBeRefreshed(token, DateUtil.tomorrow())).isFalse();
    }

    @Test
    void notExpiredCanBeRefreshed() {
        when(timeProviderMock.now())
            .thenReturn(DateUtil.now());
        String token = createToken();
        assertThat(jwtTokenService.canTokenBeRefreshed(token, DateUtil.yesterday())).isTrue();
    }

    @Test
    void canRefreshToken() {
        when(timeProviderMock.now())
            .thenReturn(DateUtil.now())
            .thenReturn(DateUtil.tomorrow());
        String firstToken = createToken();
        String refreshedToken = jwtTokenService.refreshToken(firstToken);
        Date firstTokenDate = jwtTokenService.getIssuedAtDateFromToken(firstToken);
        Date refreshedTokenDate = jwtTokenService.getIssuedAtDateFromToken(refreshedToken);
        assertThat(firstTokenDate).isBefore(refreshedTokenDate);
    }

    @Test
    void canValidateToken() {
        when(timeProviderMock.now())
            .thenReturn(DateUtil.now());
        TrustUser trustUserDetails = mock(TrustUser.class);
        when(trustUserDetails.getUsername()).thenReturn(TEST_USERNAME);

        String token = createToken();
        assertThat(jwtTokenService.validateToken(token, trustUserDetails)).isTrue();
    }

    private Map<String, Object> createClaims(String creationDate) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtTokenService.CLAIM_KEY_USERNAME, TEST_USERNAME);
        claims.put(JwtTokenService.CLAIM_KEY_AUDIENCE, "testAudience");
        claims.put(JwtTokenService.CLAIM_KEY_CREATED, DateUtil.parseDatetime(creationDate));
        return claims;
    }

    private String createToken() {
        return jwtTokenService.generateToken(TrustUser.builder().username(TEST_USERNAME).id(USER_ID).build());
    }

}