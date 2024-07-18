package io.messageservice.api.business.operators;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.messageservice.api.business.model.dto.inport.SlackMessageReserveCommand;
import io.messageservice.api.business.model.dto.outport.SlackMessageReserveInfo;
import io.messageservice.api.business.model.entity.SlackMessage;
import io.messageservice.api.business.persistence.SlackMessageReserveRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Component
@RequiredArgsConstructor
public class SlackMessageReservationManager {

	private final SlackMessageReserveRepository slackMessageReserveRepository;

	@Transactional
	public SlackMessageReserveInfo reserve(SlackMessageReserveCommand command) {
		return SlackMessageReserveInfo.from(slackMessageReserveRepository.save(command.toEntity().init()));
	}

	@Transactional(readOnly = true)
	public SlackMessageReserveInfo popNextReservedMessage() {

		Optional<SlackMessage> slackMessageOptional = slackMessageReserveRepository.popNextReservedMessage();
		if (slackMessageOptional.isEmpty()){
			return SlackMessageReserveInfo.empty();
		}

		return SlackMessageReserveInfo.from(slackMessageOptional.get());
	}

	@Transactional
	public void markAsSent(Long id) {
		SlackMessage message = slackMessageReserveRepository.findById(id).orElseThrow();
		message.markAsSent();
		slackMessageReserveRepository.save(message);
	}

}
