package org.webtree.trust.service;


import org.webtree.trust.domain.SocialConnectionInfo;

public interface SocialUserService{
    void addSocialConnection(SocialConnectionInfo info, String userId);
}
