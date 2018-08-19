package org.webtree.trust.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.webtree.trust.AbstractCassandraTest;
import org.webtree.trust.domain.UserLock;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by Udjin Skobelev on 16.08.2018.
 */

public class TrustUserLockRepositoryTest extends AbstractCassandraTest {

    @Autowired
    private CassandraOperations operations;
    @Autowired
    private TrustUserLockRepository repo;
    private UserLock user;

    @Before
    public void setUp() throws Exception {
        user = new UserLock("someUserName");
    }

    @After
    public void tearDown() throws Exception {
        operations.truncate(UserLock.class);
    }

    @Test
    public void shouldReturnTrueIfNotExists() {
        assertThat(repo.saveIfNotExist(user)).isTrue();
    }

    @Test
    public void shouldFindIfExists() {
        assertThat(repo.findById(user.getUsername()).isPresent()).isFalse();
        repo.saveIfNotExist(user);
        assertThat(repo.findById(user.getUsername()).isPresent()).isTrue();
    }

    @Test
    public void shouldReturnFalseIfAlreadyExists() {
        repo.saveIfNotExist(user);
        assertThat(repo.saveIfNotExist(user)).isFalse();
    }

    @Test
    public void shouldDeleteLock() {
        repo.saveIfNotExist(user);
        repo.delete(user);
        assertThat(repo.findById(user.getUsername()).isPresent()).isFalse();
    }
}