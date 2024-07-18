package io.messageservice.api.interfaces.poller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.messageservice.api.application.service.SlackMessageService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Component
@RequiredArgsConstructor
public class SlackMessagePollingProcessor {

	private final SlackMessageService slackMessageService;


	@Value("${slack.message.polling.interval:100}")  // 폴링 간격 (기본값: 100ms)
	private long pollingInterval;

	public void startPolling() {
		while (true) {
			try {
				slackMessageService.sendReservedMessages();
				Thread.sleep(pollingInterval);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
		}
	}

}
