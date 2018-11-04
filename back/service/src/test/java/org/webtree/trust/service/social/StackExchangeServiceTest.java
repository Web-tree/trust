package org.webtree.trust.service.social;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.webtree.trust.data.repository.social.stackexchange.StackExchangeRepository;
import org.webtree.trust.domain.StackExchangeUser;
import org.webtree.trust.domain.StackExchangeUser.Builder;
import org.webtree.trust.service.IdService;
import org.webtree.trust.service.TrustUserService;
import org.webtree.trust.service.social.api.StackExchangeApi;

@RunWith(MockitoJUnitRunner.class)
public class StackExchangeServiceTest {
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

    public StackExchangeServiceTest() {
    }

    @Before
    public void setUp() {
        this.service = new StackExchangeService(idService, trustUserService, stackExchangeApi, repo);
        this.user = Builder.asStackExchangeUser().withAccountId(STACK_USER_ID).withDisplayName(NAME).build();
    }

    @Test
    public void shouldReturnRepository() {
        Assertions.assertThat(service.getRepository()).isEqualTo(repo);
    }

    @Test
    public void shouldReturnAPi() {
        Assertions.assertThat(service.getServiceApi()).isEqualTo(stackExchangeApi);
    }

    @Test
    public void shouldReturnUsernameOfUser() {
        Assertions.assertThat(service.generateUsernameOf(user)).isEqualTo(NAME);
    }

    @Test
    public void shouldChangeSpaceToUnderscoreInNameIfUserHasIt() {
        StackExchangeUser user = Builder.asStackExchangeUser().withAccountId(STACK_USER_ID).withDisplayName("John Doe").build();
        Assertions.assertThat(service.generateUsernameOf(user)).isEqualTo("John_Doe");
    }
}
