/*
 * Copyright (c) Aqier.com 2021-2021. All rights reserved.
 */

package com.aqier.spring.cloud.common.issues1040;

import static org.springframework.core.env.StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Customized Config Center
 *
 * @author yulong.wang@Aqier.com
 * @since 2021-11-17
 */
public class CustomizedConfigCenter implements EnvironmentPostProcessor, Ordered {

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    private static final Logger LOG = DeferredLogger.getLogger(CustomizedConfigCenter.class);

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (INITIALIZED.compareAndSet(false, true)) {
            Map<String, Object> source = new HashMap<>();
            source.put("token", "1234");
            // A high-priority customized config center
            MapPropertySource customizedConfig = new MapPropertySource("customized-config-center", source);
            environment.getPropertySources().addAfter(SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, customizedConfig);
            LOG.info("A high-priority customized config center: {}", source);
        }
    }
}
