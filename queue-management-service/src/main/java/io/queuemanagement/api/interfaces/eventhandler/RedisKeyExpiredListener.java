package io.queuemanagement.api.interfaces.eventhandler;

import static io.queuemanagement.api.application.dto.request.ExpiredTokenHandlingRequest.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.queuemanagement.api.application.facade.ProcessingQueueFacade;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RedisKeyExpiredListener extends KeyExpirationEventMessageListener {

	private final ProcessingQueueFacade processingQueueFacade;
	private final BlockingQueue<String> expiredKeysQueue = new LinkedBlockingQueue<>();
	private static final int BATCH_SIZE = 10;
	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
	private final AtomicInteger counter = new AtomicInteger(0);

	public RedisKeyExpiredListener(RedisMessageListenerContainer listenerContainer, ProcessingQueueFacade processingQueueFacade) {
		super(listenerContainer);
		this.processingQueueFacade = processingQueueFacade;
		startBatchProcessor();
	}

	@Override
	@Async("redisKeyExpiredListenerExecutor")
	public void onMessage(Message message, byte[] pattern) {
		String expiredKey = new String(message.getBody());
		log.info("Expired key: {}", expiredKey);
		int i = counter.incrementAndGet();
		log.info("current consumed Counter: {}", i);

		// queueManagementFacade.completeTokens(builder().size(1).build());
		try {
			expiredKeysQueue.put(expiredKey);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Error putting key into queue", e);
		}
	}

	private void startBatchProcessor() {
		executorService.scheduleWithFixedDelay(this::processExpiredKeys, 0, 1, TimeUnit.SECONDS);
	}

	private void processExpiredKeys() {
		try {
			while (expiredKeysQueue.size() >= BATCH_SIZE) {
				List<String> keys = new ArrayList<>();
				for (int i = 0; i < BATCH_SIZE; i++) {
					keys.add(expiredKeysQueue.take());
				}
				log.info("Processing expired keys: {}", keys);
				processingQueueFacade.completeTokens(expireCommand(keys));
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("Error processing expired keys", e);
		}
	}
}
