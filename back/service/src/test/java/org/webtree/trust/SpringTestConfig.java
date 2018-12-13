package org.webtree.trust;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Created by Udjin Skobelev on 05.12.2018.
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan("org.webtree.trust.service")
public class SpringTestConfig { }