package org.webtree.trust.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.webtree.trust.SpringTestConfig;
import org.webtree.trust.data.repository.ApplicationRepository;
import org.webtree.trust.data.repository.TrustUserLockRepository;
import org.webtree.trust.data.repository.TrustUserRepository;
import org.webtree.trust.data.repository.social.facebook.FacebookRepository;
import org.webtree.trust.data.repository.social.stackexchange.StackExchangeRepository;
import org.webtree.trust.service.social.SocialServicesProvider;

/**
 * Created by Udjin Skobelev on 05.12.2018.
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringTestConfig.class)
@ActiveProfiles("test")
public abstract class AbstractSpringTest {
    @MockBean protected TrustUserRepository trustUserRepository;
    @MockBean protected FacebookRepository facebookRepository;
    @MockBean protected TrustUserLockRepository lockRepository;
    @MockBean protected StackExchangeRepository stackExchangeRepository;
    @MockBean protected CassandraOperations cassandraOperations;
    @MockBean protected ApplicationRepository applicationRepository;
    @MockBean private ModelMapper modelMapper;
    @MockBean private SocialServicesProvider provider;
}