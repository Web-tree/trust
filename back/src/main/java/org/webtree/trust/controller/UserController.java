package org.webtree.trust.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webtree.trust.domain.AuthDetals;
import org.webtree.trust.domain.User;

import org.webtree.trust.service.UserService;

@RestController
@RequestMapping("/rest/user")
public class UserController extends AbstractController {

    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("register")
    public ResponseEntity<?> doRegister( @RequestBody AuthDetals authDetals) {
        User user = modelMapper.map(authDetals, User.class);
        user.enable();
        if (userService.saveIfNotExists(user)) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body("User with this username already exists.");
        }
    }
}
