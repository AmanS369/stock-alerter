package com.stockalert.alert_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.stockalert")
@EnableJpaRepositories(basePackages = "com.stockalert.repository")
@EntityScan(basePackages = "com.stockalert.entity")
public class AlertApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertApiApplication.class, args);
	}

}
