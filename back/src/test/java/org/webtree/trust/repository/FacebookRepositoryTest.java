package org.webtree.trust.repository;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.trust.AbstractCassandraTest;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.repository.social.FacebookRepository;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FacebookRepositoryTest extends AbstractCassandraTest {

    @Autowired
    private FacebookRepository facebookRepository;
    private FacebookUser facebookUser;

    @Before
    public void setUp() throws Exception {
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
        assertThat(foundUser).isEqualTo(Optional.of(facebookUser));
    }

    @Test
    public void shouldReturnFalseIfUserExists() {
        facebookRepository.save(facebookUser);
        boolean isSaved = facebookRepository.saveIfNotExists(facebookUser);
        assertThat(isSaved).isFalse();
        Optional<FacebookUser> foundUser = facebookRepository.findById(facebookUser.getId());
        assertThat(foundUser).isEqualTo(Optional.of(facebookUser));
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