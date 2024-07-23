package io.redisservice.common.alert.publisher;

import org.springframework.context.ApplicationEventPublisher;

import io.redisservice.common.alert.GlobalExceptionAlertEvent;
import io.redisservice.common.annotation.InternalPublisher;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@InternalPublisher
@RequiredArgsConstructor
public class GlobalExceptionAlertInternalPublisher {
	private final ApplicationEventPublisher applicationEventPublisher;

	public void publish(GlobalExceptionAlertEvent alertEvent) {
		applicationEventPublisher.publishEvent(alertEvent);
	}
}
