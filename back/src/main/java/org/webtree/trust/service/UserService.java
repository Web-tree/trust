package org.webtree.trust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webtree.trust.domain.User;
import org.webtree.trust.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findById(username).get();
    }

    public boolean saveIfNotExists(User user) {
        return userRepository.saveIfNotExists(user);
    }


    //TODO So, do we now use only below method or findByUsername to , coz they are the same by functionality?
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).get();
    }


}
