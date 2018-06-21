package org.webtree.trust.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.cassandra.CassandraDataAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration(exclude = CassandraDataAutoConfiguration.class )
@ComponentScan("org.webtree.trust")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
