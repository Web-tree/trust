package org.webtree.trust.config;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


import org.springframework.security.crypto.password.PasswordEncoder;

import org.webtree.trust.domain.User;
import org.webtree.trust.domain.AuthDetals;


@ComponentScan("org.webtree.trust")
@Configuration()
public class ModelMapperConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public ModelMapper mapper() {
        ModelMapper modelMapper = new ModelMapper();
        /* modelMapper.addConverter(new UserDTOToUserConverter(passwordEncoder()));*/
        modelMapper.createTypeMap(AuthDetals.class, User.class).addMappings(
                mapper ->
                        mapper
                                .using(ctx -> {
                                    String encodedPass = ctx.getSource().toString();
                                    return passwordEncoder.encode(encodedPass);
                                })
                                .map(AuthDetals::getPassword, User::setPassword)
        );
        return modelMapper;
    }
}
