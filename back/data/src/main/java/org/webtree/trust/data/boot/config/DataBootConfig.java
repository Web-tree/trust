package org.webtree.trust.data.boot.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@ComponentScan("org.webtree.trust.data")
@EnableCassandraRepositories(basePackages = "org.webtree.trust.data.repository")
@Import(CassandraConfig.class)
public class DataBootConfig {
}
