package org.webtree.trust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.webtree.trust.domain.User;
import org.webtree.trust.repository.UserRepository;

@Service
public class UserService {

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

    public boolean saveIfNotExists(User user){
        return userRepository.saveIfNotExists(user);
    }

    /* public boolean isUserExistsByUsername(User user) {
        return userRepository.findByUsername(user.getUsername()) != null;
    }*/
}
