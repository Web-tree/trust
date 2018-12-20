package org.webtree.trust.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.webtree.trust.AbstractCassandraTest;
import org.webtree.trust.data.repository.TrustUserLockRepository;
import org.webtree.trust.domain.UserLock;


/**
 * Created by Udjin Skobelev on 16.08.2018.
 */

class TrustUserLockRepositoryTest extends AbstractCassandraTest {

    @Autowired
    private CassandraOperations operations;
    @Autowired
    private TrustUserLockRepository repo;
    private UserLock user;

    @BeforeEach
    void setUp() {
        user = new UserLock("someUserName");
    }

    @AfterEach
    void tearDown() {
        operations.truncate(UserLock.class);
    }

    @Test
    void shouldReturnTrueIfNotExists() {
        assertThat(repo.saveIfNotExist(user)).isTrue();
    }

    @Test
    void shouldFindIfExists() {
        assertThat(repo.findById(user.getUsername()).isPresent()).isFalse();
        repo.saveIfNotExist(user);
        assertThat(repo.findById(user.getUsername()).isPresent()).isTrue();
    }

    @Test
    void shouldReturnFalseIfAlreadyExists() {
        repo.saveIfNotExist(user);
        assertThat(repo.saveIfNotExist(user)).isFalse();
    }

    @Test
    void shouldDeleteLock() {
        repo.saveIfNotExist(user);
        repo.delete(user);
        assertThat(repo.findById(user.getUsername()).isPresent()).isFalse();
    }
}