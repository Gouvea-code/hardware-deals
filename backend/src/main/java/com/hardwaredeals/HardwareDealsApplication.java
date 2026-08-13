package com.hardwaredeals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import com.hardwaredeals.collector.CollectorProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(CollectorProperties.class)
public class HardwareDealsApplication {

    public static void main(String[] args) {
        SpringApplication.run(HardwareDealsApplication.class, args);
    }
}
