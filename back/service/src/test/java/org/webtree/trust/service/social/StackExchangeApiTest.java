package org.webtree.trust.service.social;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.webtree.social.stackexchange.api.impl.StackExchangeTemplate;
import org.webtree.social.stackexchange.domain.NetworkUser;
import org.webtree.social.stackexchange.domain.Site;
import org.webtree.social.stackexchange.domain.User;
import org.webtree.trust.domain.StackExchangeUser;
import org.webtree.trust.service.social.api.StackExchangeApi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class StackExchangeApiTest {
    private final static String SITE_API_PARAMETR = "stackoverflow";
    private final static String SITE_URL = "stack";
    private final static Integer ACCOUNT_ID = 556677;
    private final static Integer USER_ID = 556677;
    private final static String DISPLAY_NAME = "Jim";

    private StackExchangeApi api;

    @Mock
    private StackExchangeTemplateFactory factory;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private StackExchangeTemplate template;

    @Mock
    private ModelMapper modelMapper;

    private List<Site> sites;
    private List<NetworkUser> networkUsers;

    @Before
    public void setUp() throws Exception {
        api = new StackExchangeApi(factory, modelMapper);
        initNetworkUsers();
        initSites();
    }

    @Test
    public void shouldReturnUser() {
        String token = "abcdefg12345";
        User u = new User(ACCOUNT_ID, USER_ID, DISPLAY_NAME);
        StackExchangeUser mockUser = buildStackExchangeUser();

        given(factory.create(token)).willReturn(template);
        given(template.siteOperations().getActualSites()).willReturn(sites);
        given(template.networkUserOperations().getUserAssociatedAccounts()).willReturn(networkUsers);
        given(template.userOperations().getUserProfileBySiteName(SITE_API_PARAMETR)).willReturn(Optional.of(u));
        given(modelMapper.map(u, StackExchangeUser.class)).willReturn(mockUser);

        StackExchangeUser user = api.getUser(token);
        assertThat(user.getId()).isEqualTo(ACCOUNT_ID.toString());
    }

    private void initSites() {
        Site stack = mock(Site.class);
        given(stack.getSiteUrl()).willReturn(SITE_URL);
        given(stack.getApiSiteParameter()).willReturn(SITE_API_PARAMETR);
        Site math = mock(Site.class);
        sites = Arrays.asList(stack, math);
    }

    private void initNetworkUsers() {
        NetworkUser u1 = mock(NetworkUser.class);
        given(u1.getReputation()).willReturn(150);
        NetworkUser u2 = mock(NetworkUser.class);
        given(u1.getReputation()).willReturn(160);
        given(u1.getSiteUrl()).willReturn(SITE_URL);
        networkUsers = Arrays.asList(u1, u2);
    }

    private StackExchangeUser buildStackExchangeUser() {
        return StackExchangeUser.Builder
                .asStackExchangeUser()
                .withAccountId(ACCOUNT_ID.toString())
                .withDisplayName(DISPLAY_NAME)
                .withUserId(USER_ID).
                        build();
    }
}