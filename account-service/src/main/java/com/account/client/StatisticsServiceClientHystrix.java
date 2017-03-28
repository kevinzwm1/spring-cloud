package com.account.client;

import com.account.domain.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by t5251637 on 9/03/2017.
 */
public class StatisticsServiceClientHystrix implements StatisticsServiceClient {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void updateStatistics(@PathVariable("accountName") String accountName, Account account) {
        log.error("updateStatistics failed!");
    }
}
