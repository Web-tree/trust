package org.webtree.trust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.service.SocialService;


@RestController
@RequestMapping("/rest/social")
public class SocialController extends AbstractController {

    private SocialService socialService;

    @Autowired
    public SocialController(SocialService service) {
        this.socialService = service;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addConnection(@RequestBody SocialConnectionInfo socialConnectionInfo, Authentication authentication) {
        TrustUser user = (TrustUser) authentication.getPrincipal();
        socialService.addSocialConnection(socialConnectionInfo, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
        }
}