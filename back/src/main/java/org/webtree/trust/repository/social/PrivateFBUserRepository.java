package org.webtree.trust.repository.social;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import org.webtree.trust.domain.FacebookUser;

@Repository
public interface PrivateFBUserRepository extends CassandraRepository<FacebookUser,String> { }
