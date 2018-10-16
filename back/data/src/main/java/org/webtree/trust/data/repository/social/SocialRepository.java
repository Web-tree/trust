package org.webtree.trust.data.repository.social;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.webtree.trust.domain.SocialUser;

/**
 * It's not interface because spring tries to implement method in repository.
 */
public abstract class SocialRepository<T extends SocialUser> implements CassandraRepository<T, String> {
    public abstract boolean saveIfNotExists(T user);
}
