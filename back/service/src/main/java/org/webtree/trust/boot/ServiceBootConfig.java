package org.webtree.trust.boot;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.webtree.trust.data.boot.config.DataBootConfig;

@Configuration
@ComponentScan("org.webtree.trust.service")
@Import(DataBootConfig.class)
public class ServiceBootConfig {
}
