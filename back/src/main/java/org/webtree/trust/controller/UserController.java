package org.webtree.trust.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.trust.domain.AuthDetails;
import org.webtree.trust.domain.TrustUser;

import org.webtree.trust.service.TrustUserService;

@RestController
@RequestMapping("/rest/user")
public class UserController extends AbstractController {

    private TrustUserService service;
    private ModelMapper modelMapper;

    @Autowired
    public UserController(TrustUserService service, ModelMapper modelMapper) {
        this.service = service;
        this.modelMapper = modelMapper;
    }

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody AuthDetails authDetails) {
        TrustUser userFromRequest = modelMapper.map(authDetails, TrustUser.class);
        TrustUser user = service.createUser(userFromRequest);

        if (service.saveIfNotExists(user)) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return ResponseEntity.badRequest().body("User with this username already exists.");
        }
    }
}
