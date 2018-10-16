package org.webtree.trust.data.repository.social.facebook;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import org.webtree.trust.domain.FacebookUser;

@Repository
public interface PrivateFbUserRepository extends CassandraRepository<FacebookUser, String> {
}
