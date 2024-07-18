package io.messageservice.api.business.persistence;

import java.util.Optional;

import io.messageservice.api.business.model.entity.SlackMessage;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
public interface SlackMessageReserveRepository {
	SlackMessage save(SlackMessage slackMessage);
	Optional<SlackMessage> findById(Long id);
	Optional<SlackMessage> popNextReservedMessage();

}
