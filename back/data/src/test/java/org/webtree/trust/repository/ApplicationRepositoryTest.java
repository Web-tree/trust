package org.webtree.trust.repository;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.trust.AbstractCassandraTest;
import org.webtree.trust.data.repository.ApplicationRepository;
import org.webtree.trust.domain.Application;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;


/**
 * Created by Udjin Skobelev on 20.11.2018.
 */

public class ApplicationRepositoryTest extends AbstractCassandraTest {
    private static final String TRUST_USER_ID = "654321123456";
    private static final String APP_ID = "someAppId";
    private static final String SECRET = "someAppSecret";
    private static final String NAME = "someAppName";

    @Autowired
    private ApplicationRepository repo;

    private Application app;

    @Before
    public void setUp() throws Exception {
        app = Application.Builder.create()
            .name(NAME)
            .clientSecret(SECRET)
            .id(APP_ID)
            .trustUserId(TRUST_USER_ID)
            .build();
    }

    @Test
    public void shouldSaveApplication() {
        repo.save(app);
        assertThat(repo.findById(APP_ID)).isEqualTo(Optional.of(app));
    }

    @Test
    public void shouldUpdateApplicationsSecret() {
        String secret = "newSecret";
        repo.save(app);
        repo.updateSecret(APP_ID, secret);
        assertThat(repo.findById(APP_ID).get().getSecret()).isEqualTo(secret);
    }

    @Test
    public void shouldReturnListOfApplications() {
        List<Application> list = buildApplications();
        repo.saveAll(list);


        List<Application> appList = repo.findAllByTrustUserId(TRUST_USER_ID);
        assertThat(appList.size()).isEqualTo(3);
        assertThat(appList).containsAll(list);
    }

    @Test
    public void shouldReturnListOfApplicationsOnlyForCurrentUser() {
        Application anotherApp = Application.Builder.create()
            .id("543")
            .trustUserId("anotherID")
            .build();

        repo.save(anotherApp);
        repo.saveAll(buildApplications());

        List<Application> appList = repo.findAllByTrustUserId(TRUST_USER_ID);
        assertThat(appList.size()).isEqualTo(3);
        assertFalse(appList.stream().anyMatch(app -> app.getId().equals("543")));
    }

    @Test
    public void shouldDeleteApp() {
        repo.save(app);
        repo.deleteById(app.getId());
        assertThat(repo.findById(app.getId()).isPresent()).isFalse();
    }

    @Test
    public void shouldUpdateNameOfApp() {
        String newName = "asdfghjkl";
        repo.save(app);
        repo.updateName(app.getId(), newName);
        assertThat(repo.findById(app.getId()).get().getName()).isEqualTo(newName);
    }


    private List<Application> buildApplications() {
        return Arrays.asList(
            Application.Builder.create().id("1").trustUserId(TRUST_USER_ID).build(),
            Application.Builder.create().id("2").trustUserId(TRUST_USER_ID).build(),
            Application.Builder.create().id("3").trustUserId(TRUST_USER_ID).build()
        );
    }
}