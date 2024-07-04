package io.apiorchestrationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApiOrchestrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiOrchestrationServiceApplication.class, args);
	}

}
