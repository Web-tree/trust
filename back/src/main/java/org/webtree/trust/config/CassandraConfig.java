package org.webtree.trust.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.QueryLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableCassandraRepositories(basePackages = "org.webtree.trust.repository")
@Profile("cassandra-active")
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Bean
    @Profile({"dev", "cassandra-test"})
    public QueryLogger queryLogger(Cluster cluster) {
        QueryLogger queryLogger = QueryLogger.builder().build();
        cluster.register(queryLogger);
        return queryLogger;
    }

    @Value("${spring.data.cassandra.port}")
    private int port;

    @Value("${spring.data.cassandra.contact-points}")
    private String contactPoints;

    @Value("${spring.data.cassandra.keyspace-name}")
    private String keySpace;

    private static final String ENTITY_BASE_PACKAGE = "org.webtree.trust.domain";

    protected String getContactPoints() {
        return contactPoints;
    }


    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    public String[] getEntityBasePackages() {
        return new String[]{ENTITY_BASE_PACKAGE};
    }

    protected int getPort() {
        return port;
    }

    protected String getKeyspaceName() {
        return keySpace;
    }


  /*  protected List<String> getStartupScripts() {

        String script = "CREATE KEYSPACE IF NOT EXISTS "
                + keySpace
                + " WITH durable_writes = true "
                + "AND replication = { 'replication_factor' : 1, 'class' : 'SimpleStrategy' };";

        return Arrays.asList(script);
    }*/

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        CreateKeyspaceSpecification specification = CreateKeyspaceSpecification.createKeyspace(keySpace)
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .ifNotExists()
                .withSimpleReplication(1L);
        return Arrays.asList(specification);
    }


  /*  protected List<String> getShutdownScripts() {
        return Arrays.asList("DROP KEYSPACE my_keyspace;");
    }*/
}