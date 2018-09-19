package org.webtree.trust.mapper;

import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.webtree.trust.AbstractSpringTest;
import org.webtree.trust.domain.AuthDetails;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.repository.TrustUserLockRepository;
import org.webtree.trust.repository.social.FacebookRepository;
import org.webtree.trust.repository.TrustUserRepository;
import org.webtree.trust.repository.social.PrivateFbUserRepository;
import org.webtree.trust.util.ObjectBuilderHelper;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthDetailsMapperTest extends AbstractSpringTest {

    @MockBean
    private TrustUserRepository trustUserRepository;
    @MockBean
    private FacebookRepository userRepository;
    @MockBean
    private PrivateFbUserRepository privateFbUserRepository;
    @MockBean
    private TrustUserLockRepository lockRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectBuilderHelper builderHelper = new ObjectBuilderHelper();

    @Test
    public void checkUserMapping() {
        AuthDetails authDetails = builderHelper.buildAuthDetails();
        String password = authDetails.getPassword();
        TrustUser trustUser = modelMapper.map(authDetails, TrustUser.class);
        assertThat(authDetails.getUsername()).isEqualTo(trustUser.getUsername());
        assertThat(passwordEncoder.matches(password, trustUser.getPassword())).isTrue();
    }
}
