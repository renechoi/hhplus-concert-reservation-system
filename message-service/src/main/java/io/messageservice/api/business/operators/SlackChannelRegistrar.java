package io.messageservice.api.business.operators;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.messageservice.api.business.model.dto.inport.SlackChannelRegistrationCommand;
import io.messageservice.api.business.model.dto.outport.SlackChannelRegistrationInfo;
import io.messageservice.api.business.persistence.SlackChannelSaveRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Component
@RequiredArgsConstructor
public class SlackChannelRegistrar {

	private final SlackChannelSaveRepository slackChannelSaveRepository;

	@Transactional
	public SlackChannelRegistrationInfo save(SlackChannelRegistrationCommand command) {
		return SlackChannelRegistrationInfo.from(slackChannelSaveRepository.save(command.toEntity()));
	}
}
