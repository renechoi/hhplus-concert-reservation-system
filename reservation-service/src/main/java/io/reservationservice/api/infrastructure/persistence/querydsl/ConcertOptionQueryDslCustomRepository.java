package io.reservationservice.api.infrastructure.persistence.querydsl;

import java.util.List;

import io.reservationservice.api.business.domainentity.ConcertOption;
import io.reservationservice.api.business.dto.inport.ConcertOptionSearchCommand;

/**
 * @author : Rene Choi
 * @since : 2024/07/08
 */
public interface ConcertOptionQueryDslCustomRepository {
	List<ConcertOption> findMultipleByCondition(ConcertOptionSearchCommand searchCommand);

}
