package org.webtree.trust;


import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.webtree.trust.repository.UserRepository;

public abstract class AbstractCassandraTest extends AbstractSpringTest {

    @Autowired
    protected UserRepository userRepository;

    @Rule
    public ClearDBRule clearDBRule = new ClearDBRule();

    @ClassRule
    public static CassandraTestRule cassandraTestRule = new CassandraTestRule();

    private static class CassandraTestRule extends ExternalResource {
        @Override
        protected void before() throws Throwable {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra(300000L);
        }
    }

    private class ClearDBRule extends ExternalResource {

        @Override
        protected void after() {
            userRepository.deleteAll();
        }
    }


}
