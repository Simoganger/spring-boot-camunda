package com.foo.spring.boot.camunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages={"com.foo.spring.boot.camunda"})
@EnableJpaRepositories(basePackages={"com.foo.spring.boot.camunda.repository"})
@ComponentScan(basePackages = "com.foo.spring.boot.camunda")
@EntityScan("com.foo.spring.boot.camunda")
public class SpringBootCamundaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCamundaApplication.class, args);
	}

}
