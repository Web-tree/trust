package org.webtree.trust.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.webtree.trust.domain.AuthDetails;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.util.ObjectBuilderHelper;

class AuthDetailsMapperTest extends AbstractModelMapperTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ObjectBuilderHelper builderHelper = new ObjectBuilderHelper();

    @Test
    void checkUserMapping() {
        AuthDetails authDetails = builderHelper.buildAuthDetails();
        String password = authDetails.getPassword();
        TrustUser trustUser = modelMapper.map(authDetails, TrustUser.class);
        assertThat(authDetails.getUsername()).isEqualTo(trustUser.getUsername());
        assertThat(passwordEncoder.matches(password, trustUser.getPassword())).isTrue();
    }
}