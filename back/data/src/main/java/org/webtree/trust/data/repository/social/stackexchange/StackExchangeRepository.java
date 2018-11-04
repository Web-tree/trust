package org.webtree.trust.data.repository.social.stackexchange;

import org.springframework.stereotype.Repository;
import org.webtree.trust.data.repository.social.SocialRepository;
import org.webtree.trust.domain.StackExchangeUser;

/**
 * Created by Udjin Skobelev on 22.10.2018.
 */

@Repository
public  interface StackExchangeRepository extends SocialRepository<StackExchangeUser> { }
