package org.webtree.trust.util;


import org.webtree.trust.domain.AuthDetals;
import org.webtree.trust.domain.User;

import java.util.concurrent.atomic.AtomicInteger;

public class ObjectBuilderHelper {

    private static final String PASSWORD = "pa$$word";
    private static final String USERNAME = "someUserName";

    private AtomicInteger atomicInteger;

    public ObjectBuilderHelper() {
        atomicInteger = new AtomicInteger();
    }

    public User buildRandomUser() {
        String pas = PASSWORD + atomicInteger.getAndIncrement();
        String name = USERNAME + atomicInteger.getAndIncrement();
        return User.builder().password(pas).username(name).build();
    }

    public User buildStandartUser(){
        return User.builder().password(PASSWORD).username(USERNAME).build();
    }

    public AuthDetals buildUserDTO(){
        return AuthDetals.builder().password(PASSWORD).username(USERNAME).build();
    }

}
