package org.webtree.trust.data.repository.social;

import org.springframework.data.cassandra.repository.CassandraRepository;

import org.springframework.data.repository.NoRepositoryBean;
import org.webtree.trust.domain.SocialUser;


@NoRepositoryBean
public interface SocialRepository<T extends SocialUser>
        extends CassandraRepository<T, String>, SocialRepositoryOperations<T> { }
