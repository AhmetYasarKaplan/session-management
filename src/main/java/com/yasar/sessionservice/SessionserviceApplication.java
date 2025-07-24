package com.yasar.sessionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class SessionserviceApplication {

        public static void main(String[] args) {
                SpringApplication.run(SessionserviceApplication.class, args);
                log.info("Session Service started");
        }

}
