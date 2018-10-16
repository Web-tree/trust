package org.webtree.trust.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.trust.AbstractCassandraTest;
import org.webtree.trust.data.repository.social.facebook.FacebookRepository;
import org.webtree.trust.domain.FacebookUser;

import java.util.Optional;

public class FacebookRepositoryTest extends AbstractCassandraTest {

    @Autowired
    private FacebookRepository facebookRepository;
    private FacebookUser facebookUser;

    @Before
    public void setUp() {
        facebookUser = FacebookUser.builder().id("randomId").firstName("aliBaba").trustUserId("qwerty").build();
    }

    @Test
    public void shouldSaveAndFetchUser() {
        facebookRepository.save(facebookUser);
        FacebookUser user = facebookRepository.findById(facebookUser.getId()).get();
        assertThat(facebookUser).isEqualTo(user);
    }

    @Test
    public void shouldSaveAndReturnTrueIfUserDoesNotExist() {
        boolean isSaved = facebookRepository.saveIfNotExists(facebookUser);
        assertThat(isSaved).isTrue();
        Optional<FacebookUser> foundUser = facebookRepository.findById(facebookUser.getId());
        Assertions.assertThat(foundUser).isEqualTo(Optional.of(facebookUser));
    }

    @Test
    public void shouldReturnFalseIfUserExists() {
        facebookRepository.save(facebookUser);
        boolean isSaved = facebookRepository.saveIfNotExists(facebookUser);
        assertThat(isSaved).isFalse();
        Optional<FacebookUser> foundUser = facebookRepository.findById(facebookUser.getId());
        Assertions.assertThat(foundUser).isEqualTo(Optional.of(facebookUser));
    }

    @Test
    public void shouldNotUpdateUserWhenSavingIfExists() {
        String originalFirstName = facebookUser.getFirstName();
        facebookRepository.save(facebookUser);
        facebookUser.setFirstName("monkey");
        facebookRepository.saveIfNotExists(facebookUser);
        Optional<FacebookUser> foundUser = facebookRepository.findById(facebookUser.getId());
        assertThat(foundUser.get().getFirstName()).isEqualTo(originalFirstName);
    }
}