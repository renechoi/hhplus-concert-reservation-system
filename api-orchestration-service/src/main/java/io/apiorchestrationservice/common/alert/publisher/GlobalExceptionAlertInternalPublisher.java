package io.apiorchestrationservice.common.alert.publisher;

import org.springframework.context.ApplicationEventPublisher;

import io.apiorchestrationservice.common.alert.GlobalExceptionAlertEvent;
import io.apiorchestrationservice.common.annotation.InternalPublisher;
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
