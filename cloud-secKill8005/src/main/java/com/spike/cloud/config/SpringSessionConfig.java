package com.spike.cloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;

/**
 * @Author spike
 * @Date: 2020-05-19 15:57
 */
@Configuration
@EnableRedisHttpSession
public class SpringSessionConfig {
    private Logger log = LoggerFactory.getLogger(SpringSessionConfig.class);

//    @EventListener
//    public void onSessionExpired(SessionExpiredEvent event) {
//        String sessionId = event.getSessionId();
//        log.info("session expired: " + sessionId);
//    }
//    @EventListener
//    public void onSessionCreated(SessionCreatedEvent event) {
//        String sessionId = event.getSessionId();
//        log.info("session created: " + sessionId);
//    }
//    @EventListener
//    public void onSessionDeleted(SessionDeletedEvent event) {
//        String sessionId = event.getSessionId();
//        log.info("session deleted: " + sessionId);
//    }
}
