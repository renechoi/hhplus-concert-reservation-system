package io.queuemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableFeignClients
@EnableAsync
@EnableSchedulerLock(defaultLockAtMostFor = "PT30S")
public class QueueManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueueManagementApplication.class, args);
	}

}
