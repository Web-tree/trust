package org.webtree.trust.domain;

/**
 * Created by Udjin Skobelev on 20.08.2018.
 */
public interface SocialUser {
    String getId();

    String getTrustUserId();

    void setTrustUserId(String id);
}
