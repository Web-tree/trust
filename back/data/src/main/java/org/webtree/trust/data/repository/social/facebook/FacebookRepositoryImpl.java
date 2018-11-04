package org.webtree.trust.data.repository.social.facebook;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.webtree.trust.data.repository.social.SocialRepositoryOperationsImpl;
import org.webtree.trust.domain.FacebookUser;

/**
 * Created by Udjin Skobelev on 24.10.2018.
 */
public class FacebookRepositoryImpl extends SocialRepositoryOperationsImpl<FacebookUser> {
    public FacebookRepositoryImpl(CassandraOperations operations) {
        super(operations);
    }
}
