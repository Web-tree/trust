package org.webtree.trust.data.repository.social;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.webtree.trust.domain.SocialUser;


/**
 * Created by Udjin Skobelev on 24.10.2018.
 */

public class SocialRepositoryOperationsImpl<T extends SocialUser> implements SocialRepositoryOperations<T> {

    private CassandraOperations operations;

    public SocialRepositoryOperationsImpl(CassandraOperations operations) {
        this.operations = operations;
    }

    @Override
    public boolean saveIfNotExists(T user) {
        return operations.insert(user, InsertOptions.builder().withIfNotExists().build()).wasApplied();
    }
}
