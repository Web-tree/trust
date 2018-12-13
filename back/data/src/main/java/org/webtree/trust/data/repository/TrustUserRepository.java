package org.webtree.trust.data.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.webtree.trust.domain.TrustUser;

import java.util.Optional;

@Repository
public interface TrustUserRepository extends CassandraRepository<TrustUser, String> {

    @Query("INSERT INTO trust_user (username, password, id) " +
        "VALUES (:#{#trustUser.username}, :#{#trustUser.password},:#{#trustUser.id}) IF NOT EXISTS")
    boolean saveIfNotExists(@Param("trustUser") TrustUser trustUser);

    Optional<TrustUser> findByUsername(String username);
}
