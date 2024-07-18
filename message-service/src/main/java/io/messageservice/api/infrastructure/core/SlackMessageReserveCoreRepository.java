package io.messageservice.api.infrastructure.core;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import io.messageservice.api.business.model.entity.SlackMessage;
import io.messageservice.api.business.persistence.SlackMessageReserveRepository;
import io.messageservice.api.infrastructure.orm.SlackMessageJpaRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
@Repository
@RequiredArgsConstructor
public class SlackMessageReserveCoreRepository implements SlackMessageReserveRepository {
	private final SlackMessageJpaRepository slackMessageJpaRepository;

	@Override
	public SlackMessage save(SlackMessage slackMessage) {
		return slackMessageJpaRepository.save(slackMessage);
	}


	@Override
	public Optional<SlackMessage> findById(Long id) {
		return slackMessageJpaRepository.findById(id);
	}

	/**
	 * 추상 자료형(ADT)에서의 pop 연산 특성을 고려,
	 * pop 기능을 제공하기 위해 repository의 책임으로 mark 연산을 수행합니다.
	 */
	@Override
	public Optional<SlackMessage> popNextReservedMessage() {
		Optional<SlackMessage> slackMessage = slackMessageJpaRepository.findFirstBySentFalseAndPoppedFalseOrderByReservedAtAsc();
		slackMessage.ifPresent(message -> {
			message.markAsPopped();
			slackMessageJpaRepository.save(message);
		});
		return slackMessage;
	}


}
