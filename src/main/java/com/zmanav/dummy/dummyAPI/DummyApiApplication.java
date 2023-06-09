package com.zmanav.dummy.dummyAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class DummyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DummyApiApplication.class, args);
	}

	@GetMapping("/")
	public String hi() {
		return "Booting Spring Boot";
	}

}
