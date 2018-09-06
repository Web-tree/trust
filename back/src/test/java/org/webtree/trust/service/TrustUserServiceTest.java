package org.webtree.trust.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.domain.UserLock;
import org.webtree.trust.exception.UserAlreadyHasIdException;
import org.webtree.trust.repository.TrustUserLockRepository;
import org.webtree.trust.repository.TrustUserRepository;
import org.webtree.trust.util.ObjectBuilderHelper;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class TrustUserServiceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Mock
    private TrustUserRepository repo;
    @Mock
    private TrustUserLockRepository lockRepo;
    @Mock
    private IdService idService;

    private TrustUserService service;
    private ObjectBuilderHelper objectBuilderHelper = new ObjectBuilderHelper();
    private TrustUser user;

    @Before
    public void setUp() {
        service = new TrustUserService(repo, lockRepo, idService);
        user = objectBuilderHelper.buildNewUser();
    }

    @Test
    public void shouldCallRepositoryAndReturnSavedUser() {
        given(repo.save(user)).willReturn(user);
        TrustUser secondTrustUser = service.save(user);
        assertThat(user).isEqualTo(secondTrustUser);
    }

    @Test
    public void shouldReturnUserByUsername() {
        given(repo.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        TrustUser loadedByUsername = service.loadUserByUsername(user.getUsername());
        assertThat(loadedByUsername).isEqualTo(user);
    }

    @Test
    public void shouldReturnFalseIfUserDoNotExistButCantMakeLock() {
        given(repo.existsByUsername(user.getUsername())).willReturn(false);
        given(lockRepo.saveIfNotExist(any(UserLock.class))).willReturn(false);
        assertThat(service.saveIfNotExists(user)).isFalse();
        verify(repo, never()).save(user);
    }

    @Test
    public void shouldReturnFalseIfUserExists() {
        given(repo.existsByUsername(user.getUsername())).willReturn(true);
        assertThat(service.saveIfNotExists(user)).isFalse();
        verifyNoMoreInteractions(lockRepo);
        verify(repo, never()).save(user);
    }

    @Test
    public void shouldReturnTrueIfUserDoesNotExists() {
        given(repo.existsByUsername(user.getUsername())).willReturn(false);
        given(lockRepo.saveIfNotExist(any(UserLock.class))).willReturn(true);
        assertThat(service.saveIfNotExists(user)).isTrue();
        verify(repo).save(user);
    }

    @Test
    public void shouldGenerateIdIfItIsNull() {
        String id = "someId";
        given(idService.generateId()).willReturn(id);
        TrustUser newUser = service.createUser(user);
        assertThat(newUser.getId()).isEqualTo(id);
        assertThat(newUser).isEqualTo(user);
    }

    @Test(expected = UserAlreadyHasIdException.class)
    public void shouldThrowExceptionIfIdIsNotNull() {
        user.setId("someID");
        service.createUser(user);
    }

    @Test
    public void shouldFindById() {
        given(repo.findById(user.getId())).willReturn(Optional.of(user));
        assertThat(service.findById(user.getId()).get()).isEqualTo(user);
    }
}