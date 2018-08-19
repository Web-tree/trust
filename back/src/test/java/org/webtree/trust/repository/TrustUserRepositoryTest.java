package org.webtree.trust.repository;


import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.trust.AbstractCassandraTest;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.util.ObjectBuilderHelper;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class TrustUserRepositoryTest extends AbstractCassandraTest {

    @Autowired
    private TrustUserRepository repo;

    private ObjectBuilderHelper helper = new ObjectBuilderHelper();

    private TrustUser user;

    @Before
    public void setUp() {
        user = helper.buildNewUserWithId();
    }

    @Test
    public void shouldSaveAndFetchUser() {
        repo.save(user);
        Optional<TrustUser> trustUser = repo.findById(user.getId());
        assertThat(user).isEqualTo(trustUser.get());
    }

    @Test
    public void whenDeleteUserItShouldBeDeleted() {
        TrustUser savedUser = repo.save(user);
        assertThat(repo.findByUsername(savedUser.getUsername()).isPresent()).isTrue();
        repo.delete(user);
        assertThat(repo.existsById(user.getUsername())).isFalse();
    }

    @Test
    public void shouldFindUserByUserName() {
        TrustUser savedUser = repo.save(user);
        Optional<TrustUser> foundUser = repo.findByUsername(savedUser.getUsername());
        assertThat(foundUser).isEqualTo(Optional.of(savedUser));
    }

    @Test
    public void shouldFindUserById() {
        TrustUser savedUser = repo.save(user);
        Optional<TrustUser> foundUser = repo.findById(savedUser.getId());
        assertThat(foundUser).isEqualTo(Optional.of(savedUser));
    }

    @Test
    public void shouldReturnTrueIfUserDoesNotExist() {
        assertThat(repo.existsById(user.getUsername())).isFalse();
        assertThat(repo.saveIfNotExists(user)).isTrue();
        }

    @Test
    public void shouldReturnFalseIfUserExists() {
        TrustUser savedUser = repo.save(user);
        assertThat(repo.existsById(savedUser.getId())).isTrue();
        assertThat(repo.saveIfNotExists(savedUser)).isFalse();
        }

    @Test
    public void shouldNotUpdateWhenInsertUserWithTheSamePrimaryKey() {
        TrustUser savedUser = repo.save(user);
        assertThat(repo.existsById(savedUser.getId())).isTrue();
        boolean isSaved = repo.saveIfNotExists(TrustUser.builder().id(user.getId()).username("someNewUsername").build());
        assertThat(isSaved).isFalse();
        assertThat(repo.findById(savedUser.getId())).isEqualTo(Optional.of(savedUser));
    }
}