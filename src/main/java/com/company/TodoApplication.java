package com.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class TodoApplication{

	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

}
