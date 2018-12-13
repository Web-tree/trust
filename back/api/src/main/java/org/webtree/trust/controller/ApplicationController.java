package org.webtree.trust.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webtree.trust.domain.Application;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.dto.JustCreatedApplication;

import org.webtree.trust.service.ApplicationService;

import java.util.List;


/**
 * Created by Udjin Skobelev on 02.11.2018.
 */

@RestController
@RequestMapping("/rest/app")
public class ApplicationController extends AbstractController {

    private ApplicationService service;

    @Autowired
    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public List<Application> getList(Authentication auth) {
        TrustUser user = (TrustUser) auth.getPrincipal();
        return service.findAllByTrustUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody String appName, Authentication auth) {
        TrustUser user = (TrustUser) auth.getPrincipal();

        JustCreatedApplication wrapper = service.create(appName, user.getId());
        service.save(wrapper.getApplication());
        return ResponseEntity.status(HttpStatus.CREATED).body(wrapper);
    }

    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestParam @P("id") String id) {
        return ResponseEntity.ok(service.resetSecretTo(id));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody String name, @RequestParam @P("id") String id) {
        service.update(id, name);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam @P("id") String id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}