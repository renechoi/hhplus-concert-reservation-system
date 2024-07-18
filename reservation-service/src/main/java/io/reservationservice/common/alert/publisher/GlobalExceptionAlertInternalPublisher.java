package io.reservationservice.common.alert.publisher;

import org.springframework.context.ApplicationEventPublisher;

import io.reservationservice.common.alert.GlobalExceptionAlertEvent;
import io.reservationservice.common.annotation.InternalPublisher;
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
