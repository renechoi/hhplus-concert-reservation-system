package io.redisservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class RedisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisServiceApplication.class, args);
	}

}
