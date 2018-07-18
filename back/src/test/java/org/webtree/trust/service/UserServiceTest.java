package org.webtree.trust.service;


import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.webtree.trust.util.ObjectBuilderHelper;

import org.webtree.trust.domain.User;
import org.webtree.trust.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository mockUserRepository;

    private UserService userService;

    private ObjectBuilderHelper objectBuilderHelper = new ObjectBuilderHelper();

    @Before
    public void setUp() throws Exception {
        userService = new UserService(mockUserRepository);
    }

    @Test
    public void shouldCallRepositoryAndReturnSavedUser() {
        //TODO Do we need to test encode functionality here ?
        User firstUser = objectBuilderHelper.buildRandomUser();
        given(mockUserRepository.save(firstUser)).willReturn(firstUser);
        User secondUser = userService.save(firstUser);
        assertThat(firstUser).isEqualTo(secondUser);
    }

    @Test
    public void shouldReturnUserByUsername() {
        User user = objectBuilderHelper.buildRandomUser();
        given(mockUserRepository.findById(user.getUsername())).willReturn(Optional.of(user));
        User foundUser = userService.findByUsername(user.getUsername());
        assertThat(foundUser).isEqualTo(user);
        //and here additional test for loadUserByUsername
        User loadedByUsername = userService.loadUserByUsername(user.getUsername());
        assertThat(loadedByUsername).isEqualTo(user);
    }

    @Test
    public void shouldReturnFalseIfUserExists() {
        User user = objectBuilderHelper.buildRandomUser();
        given(mockUserRepository.saveIfNotExists(user)).willReturn(false);
        assertThat(userService.saveIfNotExists(user)).isFalse();
    }

    @Test
    public void shouldReturnTrueIfUserDoesntExists() {
        User user = objectBuilderHelper.buildRandomUser();
        given(mockUserRepository.saveIfNotExists(user)).willReturn(true);
        assertThat(userService.saveIfNotExists(user)).isTrue();
    }
}
