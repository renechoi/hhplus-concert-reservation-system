package io.messageservice.api.business.operators;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.slack.api.Slack;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;

import io.messageservice.api.business.model.dto.inport.SlackMessageReserveCommand;
import io.messageservice.api.business.model.dto.inport.SlackMessageSendCommand;
import io.messageservice.api.business.model.entity.SlackChannel;
import io.messageservice.api.business.persistence.SlackChannelRetrieveRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Component
@RequiredArgsConstructor
public class SlackMessageSender {

	private final Slack slack = Slack.getInstance();

	private final SlackChannelRetrieveRepository slackChannelRetrieveRepository;

	@SneakyThrows
	public void send(SlackMessageSendCommand command) {
		ChatPostMessageRequest request = ChatPostMessageRequest.builder()
			.token(command.getToken())
			.channel(command.getChannelId())
			.text(command.getMessage())
			.build();

		slack.methods().chatPostMessage(request);
	}

	@SneakyThrows
	@Async("slackMessageSenderExecutor")
	public void sendAsync(SlackMessageSendCommand command) {
		ChatPostMessageRequest request = ChatPostMessageRequest.builder()
			.token(command.getToken())
			.channel(command.getChannelId())
			.text(command.getMessage())
			.build();

		slack.methods().chatPostMessage(request);
	}


}
