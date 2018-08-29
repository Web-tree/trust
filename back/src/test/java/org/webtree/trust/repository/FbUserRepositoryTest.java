package org.webtree.trust.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.trust.AbstractCassandraTest;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.repository.social.FbUserRepository;
import org.webtree.trust.util.ObjectBuilderHelper;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class FbUserRepositoryTest extends AbstractCassandraTest {

    private ObjectBuilderHelper helper = new ObjectBuilderHelper();
    @Autowired
    private FbUserRepository fbUserRepository;

    @Test
    public void shouldSaveAndFetchUser() {
        FacebookUser facebookUser = helper.buildFacebookUser();
        fbUserRepository.save(facebookUser);
        FacebookUser user = fbUserRepository.findById(facebookUser.getId()).get();
        assertThat(facebookUser).isEqualTo(user);
    }

    @Test
    public void shouldFindUserById() {
        FacebookUser user = helper.buildFacebookUser();
        fbUserRepository.save(user);
        Optional<FacebookUser> foundUser = fbUserRepository.findById(user.getId());
        assertThat(foundUser).isEqualTo(Optional.of(user));
    }

    @Test
    public void shouldSaveAndReturnTrueIfUserDoesntExist() {
        FacebookUser user = helper.buildFacebookUser();
        boolean isSaved = fbUserRepository.saveIfNotExists(user);
        assertThat(isSaved).isTrue();
        Optional<FacebookUser> foundUser = fbUserRepository.findById(user.getId());
        assertThat(foundUser).isEqualTo(Optional.of(user));
    }

    @Test
    public void shouldReturnFalseIfUserExists() {
        FacebookUser user = helper.buildFacebookUser();
        fbUserRepository.save(user);
        boolean isSaved = fbUserRepository.saveIfNotExists(user);
        assertThat(isSaved).isFalse();
        Optional<FacebookUser> foundUser = fbUserRepository.findById(user.getId());
        assertThat(foundUser).isEqualTo(Optional.of(user));
    }
}