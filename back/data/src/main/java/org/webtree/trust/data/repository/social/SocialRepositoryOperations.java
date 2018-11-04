package org.webtree.trust.data.repository.social;

/**
 * Created by Udjin Skobelev on 24.10.2018.
 */


public interface SocialRepositoryOperations<T> {
    boolean saveIfNotExists(T user);
}
