package com.powerledger.screening;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories
@EnableR2dbcAuditing
@OpenAPIDefinition(info = @Info(
		title = "Screening App",
		version = "1.0.0",
		description = "Backend APIs for managing Batteries."))
public class ScreeningApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScreeningApplication.class, args);
	}

}
