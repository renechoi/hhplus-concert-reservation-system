package io.queuemanagement.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author : Rene Choi
 * @since : 2024/06/27
 */
@Configuration
public class AsyncConfig implements AsyncConfigurer {

	private Executor createTaskExecutor(String threadNamePrefix) {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50);
		executor.setMaxPoolSize(100);
		executor.setQueueCapacity(5000);
		executor.setThreadNamePrefix(threadNamePrefix);
		executor.initialize();
		return executor;
	}


	@Bean(name = "redisKeyExpiredListenerExecutor")
	public Executor redisKeyExpiredListenerExecutor() {
		return createTaskExecutor("RedisKeyExpiredListener-");
	}


}
