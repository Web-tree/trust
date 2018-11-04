package org.webtree.trust.data.repository.social.stackexchange;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.webtree.trust.data.repository.social.SocialRepositoryOperationsImpl;
import org.webtree.trust.domain.StackExchangeUser;

/**
 * Created by Udjin Skobelev on 24.10.2018.
 */
public class StackExchangeRepositoryImpl extends SocialRepositoryOperationsImpl<StackExchangeUser> {
    public StackExchangeRepositoryImpl(CassandraOperations operations) {
        super(operations);
    }
}
