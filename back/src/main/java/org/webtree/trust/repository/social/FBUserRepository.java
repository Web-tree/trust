package org.webtree.trust.repository.social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.InsertOptions;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.webtree.trust.domain.FacebookUser;

import java.util.List;
import java.util.Optional;


/**
 * Created by Udjin Skobelev on 07.08.2018.
 */

@Repository
public class FBUserRepository implements CassandraRepository<FacebookUser, String> {

    private PrivateFBUserRepository privateRepo;
    private CassandraOperations operations;

    @Autowired
    public FBUserRepository(PrivateFBUserRepository privateRepo, CassandraOperations operations) {
        this.privateRepo = privateRepo;
        this.operations = operations;
    }

    public boolean saveIfNotExists(FacebookUser user) {
        return operations.insert(user, InsertOptions.builder().withIfNotExists().build()).wasApplied();
    }

    public FacebookUser save(FacebookUser user) { return privateRepo.save(user); }

    public Optional<FacebookUser> findById(String id) { return privateRepo.findById(id); }
    @Override
    public boolean existsById(String s) { return privateRepo.existsById(s); }

    @Override
    public <S extends FacebookUser> List<S> saveAll(Iterable<S> iterable) { return privateRepo.saveAll(iterable); }

    @Override
    public List<FacebookUser> findAll() { return privateRepo.findAll(); }

    @Override
    public List<FacebookUser> findAllById(Iterable<String> iterable) { return privateRepo.findAllById(iterable); }

    @Override
    public long count() { return privateRepo.count(); }

    @Override
    public void deleteById(String s) { privateRepo.deleteById(s); }

    @Override
    public void delete(FacebookUser user) { privateRepo.delete(user); }

    @Override
    public void deleteAll(Iterable<? extends FacebookUser> iterable) { privateRepo.deleteAll(iterable); }

    @Override
    public void deleteAll() { privateRepo.deleteAll(); }

    @Override
    public Slice<FacebookUser> findAll(Pageable pageable) { return privateRepo.findAll(pageable); }

    @Override
    public <S extends FacebookUser> S insert(S s) { return privateRepo.insert(s); }

    @Override
    public <S extends FacebookUser> List<S> insert(Iterable<S> iterable) { return privateRepo.insert(iterable); }
}
