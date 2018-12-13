package org.webtree.trust.service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.webtree.trust.data.repository.ApplicationRepository;
import org.webtree.trust.domain.Application;
import org.webtree.trust.dto.JustCreatedApplication;
import org.webtree.trust.service.security.CombinedPasswordEncoder;


import java.util.List;
import java.util.Optional;


/**
 * Created by Udjin Skobelev on 02.11.2018.
 */

@Service
public class ApplicationService {
    private ApplicationRepository repo;
    private CombinedPasswordEncoder encoder;
    private ApplicationFactory factory;

    @Autowired
    public ApplicationService(ApplicationRepository repo,
                              CombinedPasswordEncoder encoder,
                              ApplicationFactory factory) {
        this.repo = repo;
        this.encoder = encoder;
        this.factory = factory;
    }

    public List<Application> findAllByTrustUserId(String id) {
        return repo.findAllByTrustUserId(id);
    }

    public Optional<Application> findById(String id) {
        return repo.findById(id);
    }

    public JustCreatedApplication create(String appName, String trustUserId) {
        String secret = generateSecret();
        Application app = factory.create(appName, trustUserId, secret);
        return new JustCreatedApplication(secret, app);
    }

    public Application save(Application app) {
        return repo.save(app);
    }

    @PreAuthorize("@AppPreAuthorize.isOwner(principal,#id)")
    public String resetSecretTo( String id) {
        String newSecret = generateSecret();
        repo.updateSecret(id, encoder.encode(newSecret));
        return newSecret;
    }

    private String generateSecret() {
        return randomAlphanumeric(15, 20);
    }

    @PreAuthorize("@AppPreAuthorize.isOwner(principal,#id)")
    public void update( String id, String newName) {
        repo.updateName(id, newName);
    }

    @PreAuthorize("@AppPreAuthorize.isOwner(principal,#id)")
    public void delete( String id) {
        repo.deleteById(id);
    }
}