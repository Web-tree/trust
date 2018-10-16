package org.webtree.trust.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Udjin Skobelev on 12.08.2018.
 */
@Service
public class IdService {
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
