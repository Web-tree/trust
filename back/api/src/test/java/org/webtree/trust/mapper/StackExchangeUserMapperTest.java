package org.webtree.trust.mapper;

import org.junit.jupiter.api.Test;
import org.webtree.social.stackexchange.domain.User;
import org.webtree.trust.domain.StackExchangeUser;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Udjin Skobelev on 25.10.2018.
 */

class StackExchangeUserMapperTest extends AbstractModelMapperTest {

    @Test
    void checkUserIdMapping() {
        int accountId = 12345;
        int userId = 987654321;
        String name = "John snow";

        User user = new User(accountId, userId, name);
        StackExchangeUser stackExchangeUser = modelMapper.map(user, StackExchangeUser.class);

        assertThat(stackExchangeUser.getId()).isEqualTo(Integer.toString(accountId));
    }
}