package org.webtree.trust.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Created by Udjin Skobelev on 12.08.2018.
 */
class IdServiceTest {

    @Test
    void shouldGenerateUUID() {
        String uuid = new IdService().generateId();
        assertThat(uuid).isNotEmpty();
    }
}