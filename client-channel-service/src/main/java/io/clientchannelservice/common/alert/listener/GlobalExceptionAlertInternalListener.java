package io.clientchannelservice.common.alert.listener;

import static io.clientchannelservice.common.alert.client.messageservice.dto.SlackMessageReserveRequest.*;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

import io.clientchannelservice.common.alert.GlobalExceptionAlertEvent;
import io.clientchannelservice.common.alert.client.messageservice.MessageServiceClientAdapter;
import io.clientchannelservice.common.annotation.InternalListener;
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
