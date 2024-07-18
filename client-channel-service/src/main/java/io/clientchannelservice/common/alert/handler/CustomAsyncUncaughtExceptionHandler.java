package io.clientchannelservice.common.alert.handler;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import io.clientchannelservice.common.alert.GlobalExceptionAlertEvent;
import io.clientchannelservice.common.alert.publisher.GlobalExceptionAlertInternalPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {


	private final GlobalExceptionAlertInternalPublisher globalExceptionAlertInternalPublisher;

	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
		log.error("Thread {} threw exception: {}", Thread.currentThread().getName(), throwable.getMessage());
		globalExceptionAlertInternalPublisher.publish(new GlobalExceptionAlertEvent("uncaughtException", throwable));
	}
}