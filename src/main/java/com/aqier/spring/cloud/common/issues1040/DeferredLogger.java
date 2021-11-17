/*
 * Copyright (c) Aqier.com 2021-2021. All rights reserved.
 */

package com.aqier.spring.cloud.common.issues1040;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.SubstituteLoggingEvent;
import org.slf4j.helpers.SubstituteLogger;
import org.slf4j.helpers.SubstituteLoggerFactory;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Spring Boot 应用启动初期日志记录器
 *
 * @author yulong.wang@Aqier.com
 * @since 2021-11-17
 */
public class DeferredLogger implements ApplicationListener<ApplicationPreparedEvent> {

    private static ConfigurableApplicationContext applicationContext;

    private static final SubstituteLoggerFactory logFactory = new SubstituteLoggerFactory();

    @Override
    public void onApplicationEvent(@NonNull  ApplicationPreparedEvent event) {
        setApplicationContext(event.getApplicationContext());
        LinkedBlockingQueue<SubstituteLoggingEvent> queue = logFactory.getEventQueue();
        for (SubstituteLoggingEvent logEvent : queue) {
            SubstituteLogger logger = logEvent.getLogger();
            logger.setDelegate(LoggerFactory.getLogger(logEvent.getLoggerName()));
            logger.log(logEvent);
        }
        queue.clear();
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Logger getLogger(Class<?> clazz) {
        return logFactory.getLogger(clazz.getName());
    }

    public static void setApplicationContext(ConfigurableApplicationContext applicationContext) {
        DeferredLogger.applicationContext = applicationContext;
    }
}
