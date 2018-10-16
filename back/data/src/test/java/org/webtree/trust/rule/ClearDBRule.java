package org.webtree.trust.rule;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.rules.ExternalResource;

public class ClearDBRule extends ExternalResource {

    @Override
    protected void after() {
        cleanKeyspaces();

    }

    private void cleanKeyspaces() {
//        ResultSet keyspaces = EmbeddedCassandraServerHelper.getSession().execute("KEYSPACES");
//        keyspaces.all().stream().map(row -> row.get(0, String.class)).;
        //TODO: clean all keyspaces
        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra("trust_main");
    }
}
