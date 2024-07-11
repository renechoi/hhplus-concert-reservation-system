package io.reservationservice.api.business.persistence;

import io.reservationservice.api.business.domainentity.Concert;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
public interface ConcertRepository {
	Concert save(Concert entity);

	Concert findByIdWithThrows(Long concertId);
}
