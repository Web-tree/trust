package org.webtree.trust.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.webtree.trust.data.boot.config.DataBootConfig;

@SpringBootApplication(exclude = CassandraDataAutoConfiguration.class)
@ComponentScan({"org.webtree.trust"})
@Import({ServiceBootConfig.class, DataBootConfig.class})
@EnableCassandraRepositories(basePackages = "org.webtree.trust.data.repository")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
