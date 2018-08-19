package org.webtree.trust;


import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;

@ActiveProfiles("cassandra-test")
public abstract class AbstractCassandraTest extends AbstractSpringTest {

    @Autowired
    private Collection<CassandraRepository> repos;

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
            repos.iterator().forEachRemaining(CrudRepository::deleteAll);
            }
    }


}
