package org.webtree.trust.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.webtree.trust.service.FBUserService;
import org.webtree.trust.social.SocialServicesHolder;

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
    public SocialServicesHolder services(){
        SocialServicesHolder holder = new SocialServicesHolder();
        holder.addService("facebook", applicationContext.getBean(FBUserService.class));
        return holder;
    }
}
