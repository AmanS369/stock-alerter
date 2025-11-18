package com.stockalert.alert_matcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams

@Configuration
@EnableAutoConfiguration
@ComponentScan

public class AlertMatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertMatcherApplication.class, args);
	}

}
