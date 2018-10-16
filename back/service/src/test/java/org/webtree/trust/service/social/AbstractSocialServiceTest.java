package org.webtree.trust.service.social;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.webtree.trust.data.repository.social.SocialRepository;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.domain.SocialUser;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.exception.ProfileAlreadyLinkedWithAnotherAccountException;
import org.webtree.trust.exception.ProfileAlreadyLinkedWithCurrentAccountException;
import org.webtree.trust.exception.UnexpectedSocialRepositoryResponseException;
import org.webtree.trust.service.IdService;
import org.webtree.trust.service.TrustUserService;
import org.webtree.trust.service.social.api.SocialApi;

import java.util.Optional;

/**
 * Created by Udjin Skobelev on 09.09.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class AbstractSocialServiceTest {

    private final static String TRUST_USER_ID = "54321";
    private final static String TOKEN = "t1o2k3e4";
    private final static String USERNAME = "John";
    private final static String SOCIAL_USER_ID = "someRandomId";

    @Mock
    private SocialRepository<SocialUser> repo;
    @Mock
    private SocialApi<SocialUser> socialApi;
    @Mock
    private IdService idService;
    @Mock
    private TrustUserService trustUserService;
    private AbstractSocialService service;
    private SocialUser userFromApi;
    private SocialUser userFromDB;
    private SocialConnectionInfo info;
    private TrustUser trustUser;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        service = new AbstractSocialService(idService, trustUserService) {
            @Override
            SocialRepository getRepository() {
                return repo;
            }

            @Override
            SocialApi getServiceApi() {
                return socialApi;
            }

            @Override
            String generateUsernameOf(SocialUser user) {
                return USERNAME;
            }
        };

        info = SocialConnectionInfo.builder().token(TOKEN).build();
        trustUser = TrustUser.builder().id(TRUST_USER_ID).username(USERNAME).build();
        userFromApi = socialUserBuilder(TRUST_USER_ID);
        userFromDB = socialUserBuilder(TRUST_USER_ID);
        given(socialApi.getUser(TOKEN)).willReturn(userFromApi);
    }

    @Test
    public void shouldGetTrustUserIdFromSocialUserIfExistsAndFindTrustUserByIt() {
        given(repo.findById(userFromApi.getId())).willReturn(Optional.of(userFromDB));
        given(trustUserService.findById(userFromDB.getTrustUserId())).willReturn(Optional.of(trustUser));
        assertThat(service.login(info)).isEqualTo(trustUser);
    }

    @Test
    public void shouldReturnTrustUserFromDBIfSocialUserDoNotHaveTrustUserId() {
        String generatedId = "someRandomLongId";
        SocialUser userFromDbWithoutTrustUserId = socialUserBuilder(null);
        given(repo.findById(userFromApi.getId())).willReturn(Optional.of(userFromDbWithoutTrustUserId));
        given(idService.generateId()).willReturn(generatedId);
        given(trustUserService.findById(generatedId)).willReturn(Optional.of(trustUser));
        assertThat(service.login(info)).isEqualTo(trustUser);
        verify(repo).save(userFromApi);
    }


    @Test
    public void shouldMadeTrustUserOfSocialUserAndReturn() {
        given(repo.findById(userFromApi.getId())).willReturn(Optional.of(userFromDB));
        given(trustUserService.findById(userFromDB.getTrustUserId())).willReturn(Optional.empty());
        given(trustUserService.saveIfNotExists(any())).willReturn(true);
        TrustUser user = service.login(info);
        assertThat(user.getId()).isEqualTo(TRUST_USER_ID);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    public void shouldAppendNumberToTrustUserUsernameIfUsernameIsBoundedAndSave() {
        given(repo.findById(userFromApi.getId())).willReturn(Optional.of(userFromDB));
        given(trustUserService.findById(TRUST_USER_ID)).willReturn(Optional.empty());
        given(trustUserService.saveIfNotExists(any(TrustUser.class))).willReturn(false, true);
        TrustUser user = service.login(info);
        assertThat(user.getUsername()).isEqualTo(USERNAME + "_" + 1);
        assertThat(user.getId()).isEqualTo(TRUST_USER_ID);
    }

    @Test
    public void shouldAppendNumberAndIterateItUntilTrustUserCanBeSaved() {
        given(repo.findById(userFromApi.getId())).willReturn(Optional.of(userFromDB));
        given(trustUserService.findById(TRUST_USER_ID)).willReturn(Optional.empty());
        given(trustUserService.saveIfNotExists(any(TrustUser.class))).willReturn(false, false, true);
        TrustUser user = service.login(info);
        assertThat(user.getUsername()).isEqualTo(USERNAME + "_" + 2);
        assertThat(user.getId()).isEqualTo(TRUST_USER_ID);
    }

    @Test
    public void throwExceptionIfFBUserAlreadyLinkedWithOtherAccount() {
        given(repo.saveIfNotExists(userFromApi)).willReturn(false);
        given(repo.findById(userFromApi.getId())).willReturn(Optional.of(userFromDB));
        exception.expect(ProfileAlreadyLinkedWithAnotherAccountException.class);
        service.addSocialConnection(info, "someOtherID");
    }

    @Test
    public void throwExceptionIfFBUserAlreadyConnected() {
        given(repo.saveIfNotExists(userFromApi)).willReturn(false);
        given(repo.findById(userFromApi.getId())).willReturn(Optional.of(userFromDB));
        exception.expect(ProfileAlreadyLinkedWithCurrentAccountException.class);
        service.addSocialConnection(info, TRUST_USER_ID);
    }

    @Test
    public void shouldNotThrowExceptionIfFBUserNotExistsInDBWhenSaving() {
        given(repo.saveIfNotExists(userFromApi)).willReturn(true);
        service.addSocialConnection(info, TRUST_USER_ID);
        verify(repo, never()).findById(userFromApi.getId());
        }

    @Test
    public void shouldSaveFBUserWithTrustUserIdIfThisFieldWasEmpty() {
        SocialUser userFromDbWithoutTrustUserId = socialUserBuilder(null);
        given(repo.saveIfNotExists(userFromApi)).willReturn(false);
        given(repo.findById(userFromApi.getId())).willReturn(Optional.of(userFromDbWithoutTrustUserId));
        service.addSocialConnection(info, TRUST_USER_ID);
        verify(repo).save(userFromApi);
    }

    @Test
    public void shouldThrowExceptionIfUserNotFound() {
        given(repo.saveIfNotExists(userFromApi)).willReturn(false);
        given(repo.findById(userFromApi.getId())).willReturn(Optional.empty());
        exception.expect(UnexpectedSocialRepositoryResponseException.class);
        service.addSocialConnection(info, TRUST_USER_ID);
    }

    private SocialUser socialUserBuilder(String id) {
        return new SocialUser() {
            @Override
            public String getId() {
                return SOCIAL_USER_ID;
            }

            @Override
            public String getTrustUserId() {
                return id;
            }

            @Override
            public void setTrustUserId(String id) { }
        };
    }
}