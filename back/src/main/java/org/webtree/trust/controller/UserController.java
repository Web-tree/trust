package org.webtree.trust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webtree.trust.domain.User;
import org.webtree.trust.service.UserService;

@RestController
@RequestMapping("/rest/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<?> doRegister(@RequestBody User user) {
        if (userService.saveIfNotExists(user)) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body("User with this username already exists.");
        }
    }
}
