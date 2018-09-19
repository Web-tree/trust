package org.webtree.trust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.domain.TrustUser;

import org.webtree.trust.service.social.SocialServiceFacade;

import javax.validation.Valid;


@RestController
@RequestMapping("/rest/social")
public class SocialController extends AbstractController {

    private SocialServiceFacade socialService;

    @Autowired
    public SocialController(SocialServiceFacade socialService) {
        this.socialService = socialService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addConnection(@Valid @RequestBody SocialConnectionInfo info, Authentication auth) {
        TrustUser user = (TrustUser) auth.getPrincipal();
        socialService.addSocialConnection(info, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody SocialConnectionInfo info) {
        return ResponseEntity.ok(socialService.login(info));
    }
}