package org.webtree.trust.mapper;

import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.webtree.trust.AbstractSpringTest;
import org.webtree.trust.domain.AuthDetals;
import org.webtree.trust.domain.User;
import org.webtree.trust.repository.UserRepository;
import org.webtree.trust.util.ObjectBuilderHelper;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelMapperTest extends AbstractSpringTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    private ObjectBuilderHelper builderHelper = new ObjectBuilderHelper();

    @Test
    public void checkUserMapping() {
        AuthDetals authDetals = builderHelper.buildUserDTO();
        String password = authDetals.getPassword();
        User user = modelMapper.map(authDetals, User.class);
        assertThat(authDetals.getUsername()).isEqualTo(user.getUsername());
        assertThat(passwordEncoder.matches(password, user.getPassword())).isTrue();
    }
}
