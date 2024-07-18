package io.messageservice.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import io.messageservice.api.interfaces.poller.SlackMessagePollingProcessor;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */

@Component
@RequiredArgsConstructor
public class StartupConfig {
	private final SlackMessagePollingProcessor slackMessagePollingProcessor;

	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
		slackMessagePollingProcessor.startPolling();
	}
}
