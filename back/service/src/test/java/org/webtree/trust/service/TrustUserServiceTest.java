package org.webtree.trust.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.webtree.trust.data.repository.TrustUserLockRepository;
import org.webtree.trust.data.repository.TrustUserRepository;
import org.webtree.trust.domain.TrustUser;
import org.webtree.trust.domain.UserLock;
import org.webtree.trust.exception.UserAlreadyHasIdException;
import org.webtree.trust.util.ObjectBuilderHelper;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TrustUserServiceTest {

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
    void setUp() {
        service = new TrustUserService(repo, lockRepo,idService);
        user = helper.buildNewUser();
        }

    @Test
    void shouldCallRepositoryAndReturnSavedUser() {
        given(repo.save(user)).willReturn(user);
        TrustUser secondTrustUser = service.save(user);
        assertThat(user).isEqualTo(secondTrustUser);
    }

    @Test
    void shouldReturnUserByUsername() {
        given(repo.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        TrustUser loadedByUsername = service.loadUserByUsername(user.getUsername());
        assertThat(loadedByUsername).isEqualTo(user);
    }

    @Test
    void shouldReturnFalseIfUserDoNotExistButCantMakeLock() {
        given(repo.findByUsername(user.getUsername())).willReturn(Optional.empty());
        given(lockRepo.saveIfNotExist(any(UserLock.class))).willReturn(false);
        assertThat(service.saveIfNotExists(user)).isFalse();
        verify(repo, never()).save(user);
    }

    @Test
    void shouldReturnFalseIfUserExists() {
        given(repo.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        assertThat(service.saveIfNotExists(user)).isFalse();
        verifyNoMoreInteractions(lockRepo);
        verify(repo, never()).save(user);
    }

    @Test
    void shouldReturnTrueIfUserDoesNotExists() {
        given(repo.findByUsername(user.getUsername())).willReturn(Optional.empty());
        given(lockRepo.saveIfNotExist(any(UserLock.class))).willReturn(true);
        assertThat(service.saveIfNotExists(user)).isTrue();
        verify(repo).save(user);
    }

    @Test
    void shouldGenerateIdIfItIsNull() {
        String id = "id";
        given(idService.generateId()).willReturn(id);
        TrustUser newUser = service.createUser(user);
        assertThat(newUser).isEqualTo(user);
        assertThat(user.getId()).isEqualTo(id);
    }

    @Test
    void shouldThrowExceptionIfIdIsNotNull() {
        user.setId("someID");
        assertThatThrownBy(() -> service.createUser(user)).isInstanceOf(UserAlreadyHasIdException.class);
    }

    @Test
    void shouldFindById() {
        given(repo.findById(user.getId())).willReturn(Optional.of(user));
        //noinspection OptionalGetWithoutIsPresent
        assertThat(service.findById(user.getId()).get()).isEqualTo(user);
    }
}