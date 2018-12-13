package org.webtree.trust.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.cassandra.core.CassandraOperations;
import org.webtree.trust.AbstractSpringTest;
import org.webtree.trust.data.repository.ApplicationRepository;
import org.webtree.trust.data.repository.TrustUserLockRepository;
import org.webtree.trust.data.repository.TrustUserRepository;
import org.webtree.trust.data.repository.social.facebook.FacebookRepository;
import org.webtree.trust.data.repository.social.stackexchange.StackExchangeRepository;

//TODO: remove repos
public abstract class AbstractModelMapperTest extends AbstractSpringTest {

    @MockBean private TrustUserRepository trustUserRepository;
    @MockBean private FacebookRepository facebookRepository;
    @MockBean private TrustUserLockRepository lockRepository;
    @MockBean private StackExchangeRepository stackExchangeRepository;
    @MockBean private ApplicationRepository applicationRepository;
    @MockBean private CassandraOperations operations;

    @Autowired protected ModelMapper modelMapper;
}