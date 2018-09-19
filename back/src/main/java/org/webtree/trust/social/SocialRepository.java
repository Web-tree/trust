package org.webtree.trust.social;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.webtree.trust.domain.SocialUser;

public interface SocialRepository<T extends SocialUser> extends CassandraRepository<T, String> {
    boolean saveIfNotExists(T user);
}
