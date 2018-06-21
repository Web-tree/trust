package org.webtree.trust.repository;


import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.webtree.trust.domain.User;



@Repository
public interface UserRepository extends CassandraRepository<User,String> {

    @Query("SELECT * FROM user WHERE username = ?0")
    User findByUsername(String username);

    @Query("INSERT INTO user (username, password) VALUES (:#{#user.username}, :#{#user.password}) IF NOT EXISTS")
    boolean saveIfNotExists(@Param("user") User user);

}
