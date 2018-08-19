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
import org.webtree.trust.repository.social.FBUserRepository;
import org.webtree.trust.repository.TrustUserRepository;
import org.webtree.trust.repository.social.PrivateFBUserRepository;
import org.webtree.trust.util.ObjectBuilderHelper;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthDetailsMapperTest extends AbstractSpringTest {

    @MockBean
    private TrustUserRepository trustUserRepository;
    @MockBean
    private FBUserRepository userRepository;
    @MockBean
    private PrivateFBUserRepository privateFBUserRepository;
    @MockBean
    private TrustUserLockRepository lockRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectBuilderHelper builderHelper = new ObjectBuilderHelper();

    @Test
    public void checkUserMapping() {
        AuthDetails authDetals = builderHelper.buildAuthDetails();
        String password = authDetals.getPassword();
        TrustUser trustUser = modelMapper.map(authDetals, TrustUser.class);
        assertThat(authDetals.getUsername()).isEqualTo(trustUser.getUsername());
        assertThat(passwordEncoder.matches(password, trustUser.getPassword())).isTrue();
    }
}
