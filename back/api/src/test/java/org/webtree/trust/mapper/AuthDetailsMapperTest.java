package org.webtree.trust.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.webtree.trust.AbstractSpringTest;
import org.webtree.trust.data.repository.TrustUserLockRepository;
import org.webtree.trust.data.repository.TrustUserRepository;
import org.webtree.trust.data.repository.social.facebook.FacebookRepository;
import org.webtree.trust.data.repository.social.facebook.PrivateFbUserRepository;
import org.webtree.trust.domain.AuthDetails;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.util.ObjectBuilderHelper;

//TODO: remove repos
public class AuthDetailsMapperTest extends AbstractSpringTest {

    @MockBean private TrustUserRepository trustUserRepository;
    @MockBean private FacebookRepository facebookRepository;
    @MockBean private PrivateFbUserRepository privateFbUserRepository;
    @MockBean private TrustUserLockRepository lockRepository;

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
