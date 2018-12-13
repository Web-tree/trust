package org.webtree.trust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.webtree.trust.data.repository.TrustUserLockRepository;
import org.webtree.trust.data.repository.TrustUserRepository;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.domain.UserLock;
import org.webtree.trust.exception.UserAlreadyHasIdException;

import java.util.Optional;

@Service
public class TrustUserService implements UserDetailsService {

    private TrustUserRepository repository;
    private TrustUserLockRepository lockRepository;
    private IdService idService;

    @Autowired
    public TrustUserService(TrustUserRepository repository,
                            TrustUserLockRepository lockRepository,
                            IdService idService) {
        this.lockRepository = lockRepository;
        this.repository = repository;
        this.idService = idService;
    }

    public TrustUser save(TrustUser trustUser) {
        return repository.save(trustUser);
    }

    public boolean existsByUsername(String name) {
        return repository.findByUsername(name).isPresent();
    }

    public boolean saveIfNotExists(TrustUser user) {
        if (existsByUsername(user.getUsername())) {
            return false;
        }
        UserLock lock = new UserLock(user.getUsername());
        if (lockRepository.saveIfNotExist(lock)) {
            try {
                repository.save(user);
            } finally {
                lockRepository.delete(lock);
            }
            return true;
        }
        return false;
    }

    public TrustUser createUser(TrustUser user) {
        if (user.getId() != null) {
            throw new UserAlreadyHasIdException();
        }
        user.setId(idService.generateId());
        return user;
    }

    public Optional<TrustUser> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public TrustUser loadUserByUsername(String username) {
        Optional<TrustUser> user = repository.findByUsername(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }
}
