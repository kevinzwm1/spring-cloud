package com.account.client;

import com.account.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by t5251637 on 9/03/2017.
 */
public class AuthServiceClientHystrix implements AuthServiceClient {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void createUser(User user) {
        log.error("create user failed!");
    }
}
