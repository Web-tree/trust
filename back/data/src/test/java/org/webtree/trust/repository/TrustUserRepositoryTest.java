package org.webtree.trust.repository;


import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.trust.AbstractCassandraTest;
import org.webtree.trust.data.repository.TrustUserRepository;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.util.ObjectBuilderHelper;

import java.util.Optional;


class TrustUserRepositoryTest extends AbstractCassandraTest {

    @Autowired
    private TrustUserRepository repo;

    private ObjectBuilderHelper helper = new ObjectBuilderHelper();

    private TrustUser user;

    @BeforeEach
    void setUp() {
        user = helper.buildNewUserWithId();
    }

    @Test
    void shouldSaveAndFetchUser() {
        repo.save(user);
        Optional<TrustUser> trustUser = repo.findById(user.getId());
        //noinspection OptionalGetWithoutIsPresent
        assertThat(user).isEqualTo(trustUser.get());
    }

    @Test
    void whenDeleteUserItShouldBeDeleted() {
        TrustUser savedUser = repo.save(user);
        assertThat(repo.findByUsername(savedUser.getUsername()).isPresent()).isTrue();
        repo.delete(user);
        assertThat(repo.existsById(user.getUsername())).isFalse();
    }

    @Test
    void shouldFindUserByUserName() {
        TrustUser savedUser = repo.save(user);
        Optional<TrustUser> foundUser = repo.findByUsername(savedUser.getUsername());
        Assertions.assertThat(foundUser).isEqualTo(Optional.of(savedUser));
    }

    @Test
    void shouldFindUserById() {
        TrustUser savedUser = repo.save(user);
        Optional<TrustUser> foundUser = repo.findById(savedUser.getId());
        Assertions.assertThat(foundUser).isEqualTo(Optional.of(savedUser));
    }

    @Test
    void shouldReturnTrueIfUserDoesNotExist() {
        assertThat(repo.existsById(user.getUsername())).isFalse();
        assertThat(repo.saveIfNotExists(user)).isTrue();
        }

    @Test
    void shouldReturnFalseIfUserExists() {
        TrustUser savedUser = repo.save(user);
        assertThat(repo.existsById(savedUser.getId())).isTrue();
        assertThat(repo.saveIfNotExists(savedUser)).isFalse();
        }

    @Test
    void shouldNotUpdateWhenInsertUserWithTheSamePrimaryKey() {
        TrustUser savedUser = repo.save(user);
        assertThat(repo.existsById(savedUser.getId())).isTrue();
        boolean isSaved = repo.saveIfNotExists(TrustUser.builder().id(user.getId()).username("someNewUsername").build());
        assertThat(isSaved).isFalse();
        assertThat(repo.findById(savedUser.getId())).isEqualTo(Optional.of(savedUser));
    }
}