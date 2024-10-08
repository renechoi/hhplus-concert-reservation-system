package io.apiorchestrationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableFeignClients
public class ApiOrchestrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiOrchestrationServiceApplication.class, args);
	}

}
