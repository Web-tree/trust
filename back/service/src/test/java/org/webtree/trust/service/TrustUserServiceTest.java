package org.webtree.trust.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.webtree.trust.data.repository.TrustUserLockRepository;
import org.webtree.trust.data.repository.TrustUserRepository;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.domain.UserLock;
import org.webtree.trust.exception.UserAlreadyHasIdException;
import org.webtree.trust.util.ObjectBuilderHelper;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TrustUserServiceTest {

    private final static String ID = "id";
    private final static String USERNAME = "username";

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Mock
    private TrustUserRepository repo;
    @Mock
    private TrustUserLockRepository lockRepo;
    @Mock
    private IdService idService;

    private TrustUserService service;
    private ObjectBuilderHelper helper = new ObjectBuilderHelper();
    private TrustUser user;

    @BeforeEach
    public void setUp() {
        service = new TrustUserService(repo, lockRepo,idService);
        user = helper.buildNewUser();
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
        given(repo.findByUsername(user.getUsername())).willReturn(Optional.empty());
        given(lockRepo.saveIfNotExist(any(UserLock.class))).willReturn(false);
        assertThat(service.saveIfNotExists(user)).isFalse();
        verify(repo, never()).save(user);
    }

    @Test
    public void shouldReturnFalseIfUserExists() {
        given(repo.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        assertThat(service.saveIfNotExists(user)).isFalse();
        verifyNoMoreInteractions(lockRepo);
        verify(repo, never()).save(user);
    }

    @Test
    public void shouldReturnTrueIfUserDoesNotExists() {
        given(repo.findByUsername(user.getUsername())).willReturn(Optional.empty());
        given(lockRepo.saveIfNotExist(any(UserLock.class))).willReturn(true);
        assertThat(service.saveIfNotExists(user)).isTrue();
        verify(repo).save(user);
    }

    @Test
    public void shouldGenerateIdIfItIsNull() {
        String id = "id";
        given(idService.generateId()).willReturn(id);
        TrustUser newUser = service.createUser(user);
        assertThat(newUser).isEqualTo(user);
        assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    public void shouldThrowExceptionIfIdIsNotNull() {
        user.setId("someID");
        assertThatThrownBy(() -> service.createUser(user)).isInstanceOf(UserAlreadyHasIdException.class);
    }

    @Test
    public void shouldFindById() {
        given(repo.findById(user.getId())).willReturn(Optional.of(user));
        assertThat(service.findById(user.getId()).get()).isEqualTo(user);
    }
}