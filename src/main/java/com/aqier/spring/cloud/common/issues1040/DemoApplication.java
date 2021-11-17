/*
 * Copyright (c) Aqier.com 2021-2021. All rights reserved.
 */

package com.aqier.spring.cloud.common.issues1040;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 主程序
 *
 * @author yulong.wang@Aqier.com
 * @since 2021-11-17
 */
@RestController
@SpringBootApplication
public class DemoApplication {

    private static final Logger LOG = LoggerFactory.getLogger(DemoApplication.class);

    @Autowired
    private TextEncryptor textEncryptor;

    public static void main(String[] args) {
        // init ecrypt key
        System.setProperty("encrypt.key", "aqier.com");
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public String testOverwrittenConfig(Environment env) {
        // A high-priority unencrypted configuration: token=1234
        // At com.aqier.spring.cloud.common.issues1040.CustomizedConfigCenter

        // A low-priority configuration uses '{cipher}' encryption at application.properties
        // token=1111
        LOG.info("token = '{}', but the correct value should be '1234'.", env.getProperty("token")); //
        return "SUCCESS";
    }

    @GetMapping("/encrypt")
    public String encrypt(String text) {
        return textEncryptor.encrypt(text);
    }
}
