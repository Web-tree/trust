package org.webtree.trust.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.trust.AbstractCassandraTest;
import org.webtree.trust.data.repository.social.stackexchange.StackExchangeRepository;
import org.webtree.trust.domain.StackExchangeUser;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Udjin Skobelev on 22.10.2018.
 */

class StackExchangeRepositoryTest extends AbstractCassandraTest {

    @Autowired
    private StackExchangeRepository repo;

    private StackExchangeUser user;

    @BeforeEach
    void setUp() {
        user = StackExchangeUser.Builder.asStackExchangeUser().withDisplayName("123").withAccountId("321").build();
    }

    @Test
    void shouldSaveAndFetchUser() {
        repo.save(user);
        //noinspection OptionalGetWithoutIsPresent
        StackExchangeUser stackExchangeUser = repo.findById(user.getId()).get();
        assertThat(stackExchangeUser).isEqualTo(user);
    }

    @Test
    void shouldSaveAndReturnTrueIfUserDoesNotExist() {
        boolean isSaved = repo.saveIfNotExists(user);
        assertThat(isSaved).isTrue();
        Optional<StackExchangeUser> foundUser = repo.findById(user.getId());
        assertThat(foundUser).isEqualTo(Optional.of(user));
    }

    @Test
    void shouldReturnFalseIfUserExists() {
        repo.save(user);
        boolean isSaved = repo.saveIfNotExists(user);
        assertThat(isSaved).isFalse();
        Optional<StackExchangeUser> foundUser = repo.findById(user.getId());
        assertThat(foundUser).isEqualTo(Optional.of(user));
    }

    @Test
    void shouldNotUpdateUserWhenSavingIfExists() {
        String originalFirstName = user.getDisplayName();
        repo.save(user);
        user.setDisplayName("monkey");
        repo.saveIfNotExists(user);
        Optional<StackExchangeUser> foundUser = repo.findById(user.getId());
        //noinspection OptionalGetWithoutIsPresent
        assertThat(foundUser.get().getDisplayName()).isEqualTo(originalFirstName);
    }
}
