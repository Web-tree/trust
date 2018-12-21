package org.webtree.trust.extension;

import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class EmbededCassandraExtension implements BeforeAllCallback, AfterTestExecutionCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(60000L);
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        EmbeddedCassandraServerHelper.cleanDataEmbeddedCassandra("trust_main");
    }
}
