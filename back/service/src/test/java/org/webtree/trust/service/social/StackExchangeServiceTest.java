package org.webtree.trust.service.social;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webtree.trust.data.repository.social.stackexchange.StackExchangeRepository;
import org.webtree.trust.domain.StackExchangeUser;
import org.webtree.trust.domain.StackExchangeUser.Builder;
import org.webtree.trust.service.IdService;
import org.webtree.trust.service.TrustUserService;
import org.webtree.trust.service.social.api.StackExchangeApi;

@ExtendWith(MockitoExtension.class)
class StackExchangeServiceTest {
    private static final String NAME = "JohnDoe";
    private static final String STACK_USER_ID = "1q2w3e4r5t";
    @Mock
    private StackExchangeRepository repo;
    @Mock
    private StackExchangeApi stackExchangeApi;
    @Mock
    private IdService idService;
    @Mock
    private TrustUserService trustUserService;
    private StackExchangeService service;
    private StackExchangeUser user;

    @BeforeEach
    void setUp() {
        this.service = new StackExchangeService(idService, trustUserService, stackExchangeApi, repo);
        this.user = Builder.asStackExchangeUser().withAccountId(STACK_USER_ID).withDisplayName(NAME).build();
    }

    @Test
    void shouldReturnRepository() {
        Assertions.assertThat(service.getRepository()).isEqualTo(repo);
    }

    @Test
    void shouldReturnAPi() {
        Assertions.assertThat(service.getServiceApi()).isEqualTo(stackExchangeApi);
    }

    @Test
    void shouldReturnUsernameOfUser() {
        Assertions.assertThat(service.generateUsernameOf(user)).isEqualTo(NAME);
    }

    @Test
    void shouldChangeSpaceToUnderscoreInNameIfUserHasIt() {
        StackExchangeUser user = Builder.asStackExchangeUser().withAccountId(STACK_USER_ID).withDisplayName("John Doe").build();
        Assertions.assertThat(service.generateUsernameOf(user)).isEqualTo("John_Doe");
    }
}
