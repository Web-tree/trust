package org.webtree.trust.data.boot.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.Collections;
import java.util.List;

@Configuration
@ComponentScan(value = {"org.webtree.trust.data"})
@Profile("cassandra-active")
public class CassandraConfig extends AbstractCassandraConfiguration {

    private static final String ENTITY_BASE_PACKAGE = "org.webtree.trust.domain";
    @Value("${spring.data.cassandra.port}")
    private int port;
    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;
    @Value("${spring.data.cassandra.keyspace-name}")
    private String keySpace;

    @Bean
    @Profile({"dev", "cassandra-test"})
    public QueryLogger queryLogger(Cluster cluster) {
        QueryLogger queryLogger = QueryLogger.builder().build();
        cluster.register(queryLogger);
        return queryLogger;
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{ENTITY_BASE_PACKAGE};
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    protected String getContactPoints() {
        return contactPoints;
    }

    protected int getPort() {
        return port;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(keySpace)
            .with(KeyspaceOption.DURABLE_WRITES, true)
            .ifNotExists()
            .withSimpleReplication(1L);
        return Collections.singletonList(specification);
    }
}