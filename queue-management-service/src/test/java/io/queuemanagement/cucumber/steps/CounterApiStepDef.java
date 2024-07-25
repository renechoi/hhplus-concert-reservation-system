package io.queuemanagement.cucumber.steps;

import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java8.En;
import io.queuemanagement.api.infrastructure.entity.WaitingQueueTokenCounterEntity;
import io.queuemanagement.api.infrastructure.persistence.orm.WaitingQueueTokenCounterJpaRepository;

/**
 * @author : Rene Choi
 * @since : 2024/07/24
 */
public class CounterApiStepDef implements En {

	private WaitingQueueTokenCounterJpaRepository counterJpaRepository;
	@Autowired
	public CounterApiStepDef(WaitingQueueTokenCounterJpaRepository counterJpaRepository) {
		this.counterJpaRepository = counterJpaRepository;
		Given("counter를 미리 생성한다 - 초기값은 0", this::generateCounterInit);
	}

	private void generateCounterInit() {
		counterJpaRepository.saveAndFlush(new WaitingQueueTokenCounterEntity(1L, 0L));
	}
}
