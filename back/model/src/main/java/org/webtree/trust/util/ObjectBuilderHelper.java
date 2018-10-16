package org.webtree.trust.util;


import org.webtree.trust.domain.AuthDetails;
import org.webtree.trust.domain.FacebookUser;
import org.webtree.trust.domain.SocialConnectionInfo;
import org.webtree.trust.domain.TrustUser;

import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class ObjectBuilderHelper {

    private static final String PASSWORD = "pa$$word";
    private static final String USERNAME = "someUserName";
    private static final String FB_USER_ID = "123456789";
    private static final String PROVIDER_ID = "facebook";


    private AtomicInteger atomicInteger;

    public ObjectBuilderHelper() {
        atomicInteger = new AtomicInteger();
    }

    public TrustUser buildNewUser() {
        String pas = PASSWORD + atomicInteger.getAndIncrement();
        String name = USERNAME + atomicInteger.getAndIncrement();
        return TrustUser.builder().password(pas).username(name).build();
    }

    public TrustUser buildNewUserWithId() {
        TrustUser user = buildNewUser();
        user.setId("someId");
        return user;
    }

    public AuthDetails buildAuthDetails() {
        return AuthDetails.builder().password(PASSWORD).username(USERNAME).build();
    }

    public FacebookUser buildFacebookUser() {
        return FacebookUser.builder().firstName(USERNAME).id(FB_USER_ID).name("name").build();
    }

    public FacebookUser buildFacebookUserWithTrustUserId() {
        return FacebookUser.builder().firstName(USERNAME).id(FB_USER_ID).name("name").trustUserId("987654321").build();
    }

    public SocialConnectionInfo buildInfo() {
        SocialConnectionInfo info = new SocialConnectionInfo();
        info.setProvider(PROVIDER_ID);
        info.setToken("token");
        return info;
    }

    public AuthDetails buildUserDto() {
        return AuthDetails.builder().password(PASSWORD).username(USERNAME).build();
    }

}
