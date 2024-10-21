package com.classqr.sistema.estudiante;

import com.classqr.sistema.commons.configuration.SpringSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(
		basePackages = "com.classqr.sistema"
)
@Import(SpringSecurityConfig.class)
public class EstudianteApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstudianteApplication.class, args);
	}

}
