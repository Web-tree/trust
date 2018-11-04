package org.webtree.trust.boot.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.webtree.social.stackexchange.domain.User;
import org.webtree.trust.domain.AuthDetails;

import org.webtree.trust.domain.StackExchangeUser;
import org.webtree.trust.domain.TrustUser;

@ComponentScan("org.webtree.trust")
@Configuration()
public class ModelMapperConfig {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ModelMapperConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ModelMapper mapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        modelMapper.createTypeMap(AuthDetails.class, TrustUser.class)
            .addMappings(
                (mapper) ->
                    mapper
                        .using(ctx -> {
                            String encodedPass = ctx.getSource().toString();
                            return passwordEncoder.encode(encodedPass);
                        })
                        .map(AuthDetails::getPassword, TrustUser::setPassword)
            );

        modelMapper.createTypeMap(User.class, StackExchangeUser.class)
            .addMapping(User::getAccountId, StackExchangeUser::setId);

        return modelMapper;
    }
}