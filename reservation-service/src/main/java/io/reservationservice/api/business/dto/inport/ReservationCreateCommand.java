package io.reservationservice.api.business.dto.inport;

import io.reservationservice.api.business.dto.common.AbstractCommonRequestInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCreateCommand extends AbstractCommonRequestInfo {
	private Long concertOptionId;
	private Long seatNumber;
	private Long userId;

}
