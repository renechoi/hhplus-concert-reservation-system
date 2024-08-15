package io.reservationservice.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/15
 */
@Getter
@RequiredArgsConstructor
public enum Topic {
	CONCERT_RESERVATION_CONFIRM("concert-reservation-confirm", "Concert reservation confirmation event");

	private final String value;
	private final String description;
}