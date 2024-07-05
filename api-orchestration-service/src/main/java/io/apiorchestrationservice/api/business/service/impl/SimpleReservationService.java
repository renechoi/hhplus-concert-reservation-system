package io.apiorchestrationservice.api.business.service.impl;

import org.springframework.stereotype.Service;

import io.apiorchestrationservice.api.business.persistence.ReservationStoreRepository;
import io.apiorchestrationservice.api.business.service.ReservationService;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class SimpleReservationService implements ReservationService {
	private final ReservationStoreRepository reservationStoreRepository;
}
