package org.webtree.trust.data.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import org.webtree.trust.domain.Application;

import java.util.List;

/**
 * Created by Udjin Skobelev on 07.11.2018.
 */

@Repository
public interface ApplicationRepository extends CassandraRepository<Application, String> {
    List<Application> findAllByTrustUserId(String id);

    @Query("UPDATE application SET secret = ?1 WHERE id = ?0")
    void updateSecret(String appId, String newSecret);

    @Query("UPDATE application SET name = ?1 WHERE id = ?0")
    void updateName(String appId, String newName);
}