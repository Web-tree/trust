package org.webtree.trust.boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.webtree.trust.service.social.FacebookService;
import org.webtree.trust.service.social.SocialServicesProvider;
import org.webtree.trust.service.social.StackExchangeService;

@ComponentScan("org.webtree.trust")
@Configuration
public class SocialConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Bean
    public ConnectionFactoryLocator connectionFactoryLocator() {
        ConnectionFactoryRegistry registry = new ConnectionFactoryRegistry();
        registry.addConnectionFactory(
                new FacebookConnectionFactory(
                        environment.getProperty("spring.social.facebook.app-id"),
                        environment.getProperty("spring.social.facebook.app-secret"))
        );
        return registry;
    }

    @Bean
    public SocialServicesProvider services() {
        SocialServicesProvider holder = new SocialServicesProvider();
        holder.addService("facebook", applicationContext.getBean(FacebookService.class));
        holder.addService("stackexchange", applicationContext.getBean(StackExchangeService.class));
        return holder;
    }
}
