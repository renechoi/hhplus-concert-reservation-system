package io.redisservice.config;

import static io.redisservice.util.YmlLoader.*;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.redisservice.util.YmlLoader;

/**
 * @author : Rene Choi
 * @since : 2024/07/23
 */
@Configuration
public class RedissonConfig {


	@Bean
	public RedissonClient redissonClient() {
		Config config = new Config();
		String redisHost = getConfigMap("redis.host");
		String redisPort = getConfigMap("redis.port");
		config.useSingleServer().setAddress(String.format("redis://%s:%s", redisHost, redisPort));
		return Redisson.create(config);
	}
}