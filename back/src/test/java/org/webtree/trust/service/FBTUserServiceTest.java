package org.webtree.trust.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.exception.DataSavingException;
import org.webtree.trust.exception.ProfileAlreadyLinkedWithAnotherAccountException;
import org.webtree.trust.exception.ProfileAlreadyLinkedWithCurrentAccountException;
import org.webtree.trust.repository.social.FBUserRepository;
import org.webtree.trust.social.FacebookTemplateFactory;
import org.webtree.trust.util.ObjectBuilderHelper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


/**
 * Created by Udjin Skobelev on 07.08.2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class FBTUserServiceTest {

    private final static String TRUST_USER_ID = "54321";

    @Mock private FBUserRepository repo;
    @Mock private FacebookTemplateFactory factory;
    @Mock private FacebookTemplate template;
    @Mock private ModelMapper modelMapper;
    @Mock private User userFromFacebook;
    @Mock private UserOperations operations;

    private ObjectBuilderHelper helper = new ObjectBuilderHelper();
    private FBUserService service;
    private FacebookUser userFromApi;
    private FacebookUser userFromDB;
    private SocialConnectionInfo info;
    private Optional<FacebookUser> optional;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        service = new FBUserService(repo, modelMapper, factory);
        userFromApi = helper.buildFacebookUser();
        userFromDB = helper.buildFacebookUserWithTrustUserId();
        info = helper.buildInfo();
        optional = Optional.of(userFromDB);
        this.prepareFacebookApiResponse(info.getToken());
    }

    private void prepareFacebookApiResponse(String token) {
        given(factory.create(token)).willReturn(template);
        given(template.userOperations()).willReturn(operations);
        given(template.userOperations().getUserProfile()).willReturn(this.userFromFacebook);
        given(modelMapper.map(this.userFromFacebook, FacebookUser.class)).willReturn(userFromApi);
    }

    @Test
    public void shouldReturnUserById() {
        given(repo.findById(userFromDB.getId())).willReturn(optional);
        assertThat(service.findById(userFromDB.getId())).isEqualTo(optional);
    }

    @Test
    public void throwExceptionIfFBUserAlreadyLinkedWithOtherAccount() {
        given(repo.saveIfNotExists(userFromApi)).willReturn(false);
        given(repo.findById(userFromApi.getId())).willReturn(optional);
        exception.expect(ProfileAlreadyLinkedWithAnotherAccountException.class);
        service.addSocialConnection(info, TRUST_USER_ID);
    }

    @Test
    public void throwExceptionIfFBUserAlreadyConnected() {
        given(repo.saveIfNotExists(userFromApi)).willReturn(false);
        given(repo.findById(userFromApi.getId())).willReturn(optional);
        exception.expect(ProfileAlreadyLinkedWithCurrentAccountException.class);
        service.addSocialConnection(info, userFromDB.getTrustUserId());
    }

    @Test
    public void shouldNotThrowExceptionIfFBUserNotExistsInDBWhenSaving() {
        given(repo.saveIfNotExists(userFromApi)).willReturn(true);
        service.addSocialConnection(info, TRUST_USER_ID);
        verify(repo, Mockito.only()).saveIfNotExists(userFromApi);
    }

    @Test
    public void shouldSaveFBUserWithTrustUserIdIfThisFieldWasEmpty() {
        FacebookUser userWithoutTrustUserId = helper.buildFacebookUser();
        given(repo.saveIfNotExists(userFromApi)).willReturn(false);
        given(repo.findById(userFromApi.getId())).willReturn(Optional.of(userWithoutTrustUserId));
        service.addSocialConnection(info, TRUST_USER_ID);
        verify(repo).save(userFromApi);
    }

    @Test
    public void shouldThrowExceptionIfUserNotFound() {
        given(repo.saveIfNotExists(userFromApi)).willReturn(false);
        given(repo.findById(userFromApi.getId())).willReturn(Optional.ofNullable(null));
        exception.expect(DataSavingException.class);
        service.addSocialConnection(info, TRUST_USER_ID);
    }
}