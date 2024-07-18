package io.queuemanagement.common.alert.listener;

import static io.queuemanagement.common.alert.client.messageservice.dto.SlackMessageReserveRequest.*;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import io.queuemanagement.common.alert.GlobalExceptionAlertEvent;
import io.queuemanagement.common.alert.client.messageservice.MessageServiceClientAdapter;
import io.queuemanagement.common.alert.client.messageservice.dto.SlackMessageReserveRequest;
import io.queuemanagement.common.annotation.InternalListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@InternalListener
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionAlertInternalListener {
	private final MessageServiceClientAdapter messageServiceClientAdapter;


	@Async
	@EventListener
	public void handleGlobalExceptionAlert(GlobalExceptionAlertEvent event) {
		try{
			messageServiceClientAdapter.reserveSlackMessage(requestByGlobalExceptionAlertEvent(event));
		} catch (Exception e) {
			log.error("Failed to send slack message", e);
		}
	}
}
