package org.webtree.trust.util;


import org.webtree.trust.domain.User;

import java.util.concurrent.atomic.AtomicInteger;

public class UserHelper {

    private static final String PASSWORD = "pa$$word";
    private static final String USERNAME = "someUserName";

    private AtomicInteger atomicInteger;

    public UserHelper() {
        atomicInteger = new AtomicInteger();
    }


    public User buildUser() {
        String pas = PASSWORD + atomicInteger.getAndIncrement();
        String name = USERNAME + atomicInteger.getAndIncrement();
        return User.builder().password(pas).username(name).build();
    }

}
