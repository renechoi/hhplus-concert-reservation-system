package io.messageservice.api.infrastructure.orm;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.messageservice.api.business.model.entity.SlackMessage;

/**
 * @author : Rene Choi
 * @since : 2024/07/18
 */
public interface SlackMessageJpaRepository extends JpaRepository<SlackMessage, Long> {
	Optional<SlackMessage> findFirstBySentFalseOrderByReservedAtAsc();

	Optional<SlackMessage> findFirstBySentFalseAndPoppedFalseOrderByReservedAtAsc();
}

