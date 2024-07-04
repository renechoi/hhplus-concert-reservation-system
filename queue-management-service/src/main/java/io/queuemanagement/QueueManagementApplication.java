package io.queuemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class QueueManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueueManagementApplication.class, args);
	}

}
