package org.webtree.trust.repository;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.test.context.ActiveProfiles;
import org.webtree.trust.AbstractCassandraTest;

import org.webtree.trust.util.UserHelper;
import org.webtree.trust.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("cassandra-test")
public class UserRepositoryTest extends AbstractCassandraTest {

    @Autowired
    private UserRepository userRepository;

    private UserHelper userHelperService = new UserHelper();

    @Test
    public void shouldSaveAndFetchUser() {
        User baseUser = userHelperService.buildUser();
        User savedUser = userRepository.save(baseUser);
        User user = userRepository.findByUsername(baseUser.getUsername());
        assertThat(baseUser).isEqualTo(user);
    }

    @Test
    public void whenDeleteUserItShouldBeDeleted() {
        User savedUser = userRepository.save(userHelperService.buildUser());
        User user = userRepository.findByUsername(savedUser.getUsername());
        assertThat(savedUser).isEqualTo(user);
        userRepository.delete(savedUser);
        assertThat(userRepository.existsById(savedUser.getUsername())).isFalse();
    }

    @Test
    public void shouldFindUserByUserName() {
        User savedUser = userRepository.save(userHelperService.buildUser());
        Optional<User> foundUser = userRepository.findById(savedUser.getUsername());
        assertThat(foundUser).isEqualTo(Optional.of(savedUser));
    }

    @Test
    public void shouldReturnTrueIfUserDoesntExist() {
        User savedUser = userHelperService.buildUser();
        assertThat(userRepository.existsById(savedUser.getUsername())).isFalse();
        boolean isSaved = userRepository.saveIfNotExists(savedUser);
        assertThat(isSaved).isTrue();
        assertThat(userRepository.existsById(savedUser.getUsername())).isTrue();
    }

    @Test
    public void shouldReturnFalseIfUserExists() {
        User savedUser = userRepository.save(userHelperService.buildUser());
        assertThat(userRepository.existsById(savedUser.getUsername())).isTrue();
        boolean isSaved = userRepository.saveIfNotExists(savedUser);
        assertThat(isSaved).isFalse();
    }

    @Test
    public void shouldNotUpdateWhenInsertUserWithTheSamePrimaryKey() {
        User savedUser = userRepository.save(userHelperService.buildUser());
        assertThat(userRepository.existsById(savedUser.getUsername())).isTrue();
        boolean isSaved = userRepository.saveIfNotExists(savedUser);
        assertThat(isSaved).isFalse();
        assertThat(userRepository.findById(savedUser.getUsername())).isEqualTo(Optional.of(savedUser));
    }
}