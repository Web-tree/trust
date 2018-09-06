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
public class FbUserRepository implements CassandraRepository<FacebookUser, String> {

    private PrivateFbUserRepository privateRepo;
    private CassandraOperations operations;

    @Autowired
    public FbUserRepository(PrivateFbUserRepository privateRepo, CassandraOperations operations) {
        this.privateRepo = privateRepo;
        this.operations = operations;
    }

    public boolean saveIfNotExists(FacebookUser user) {
        return operations.insert(user, InsertOptions.builder().withIfNotExists().build()).wasApplied();
    }

    @Override
    public FacebookUser save(FacebookUser user) {
        return privateRepo.save(user);
    }

    @Override
    public Optional<FacebookUser> findById(String id) {
        return privateRepo.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return privateRepo.existsById(id);
    }

    @Override
    public <S extends FacebookUser> List<S> saveAll(Iterable<S> iterable) {
        return privateRepo.saveAll(iterable);
    }

    @Override
    public List<FacebookUser> findAll() {
        return privateRepo.findAll();
    }

    @Override
    public Slice<FacebookUser> findAll(Pageable pageable) {
        return privateRepo.findAll(pageable);
    }

    @Override
    public List<FacebookUser> findAllById(Iterable<String> iterable) {
        return privateRepo.findAllById(iterable);
    }

    @Override
    public long count() {
        return privateRepo.count();
    }

    @Override
    public void deleteById(String id) {
        privateRepo.deleteById(id);
    }

    @Override
    public void delete(FacebookUser user) {
        privateRepo.delete(user);
    }

    @Override
    public void deleteAll(Iterable<? extends FacebookUser> iterable) {
        privateRepo.deleteAll(iterable);
    }

    @Override
    public void deleteAll() {
        privateRepo.deleteAll();
    }

    @Override
    public <S extends FacebookUser> S insert(S user) {
        return privateRepo.insert(user);
    }

    @Override
    public <S extends FacebookUser> List<S> insert(Iterable<S> iterable) {
        return privateRepo.insert(iterable);
    }
}
