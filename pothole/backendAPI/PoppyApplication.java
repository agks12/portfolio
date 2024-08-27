package com.h2o.poppy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PoppyApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoppyApplication.class, args);
	}

}
