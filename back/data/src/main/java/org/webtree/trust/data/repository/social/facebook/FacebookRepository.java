package org.webtree.trust.data.repository.social.facebook;

import org.springframework.stereotype.Repository;
import org.webtree.trust.data.repository.social.SocialRepository;
import org.webtree.trust.domain.FacebookUser;

/**
 * Created by Udjin Skobelev on 07.08.2018.
 */

@Repository
public interface FacebookRepository  extends SocialRepository<FacebookUser> {

}
