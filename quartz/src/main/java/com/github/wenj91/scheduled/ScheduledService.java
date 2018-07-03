package com.github.wenj91.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledService {
    private static final Logger log = LoggerFactory.getLogger(ScheduledService.class);

    @Scheduled(cron = "0/10 * * * * *")
    public void execute(){
        log.info("ScheduledService");
    }
}
