package org.webtree.trust.service;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Udjin Skobelev on 12.08.2018.
 */
public class UUIDServiceTest {

    @Test
    public void shouldGenerateUUID() {
        String uuid = UUIDService.generateUUID();
        assertThat(uuid).isNotEmpty();
    }
}