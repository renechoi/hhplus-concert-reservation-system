package io.apiorchestrationservice.api.business.dto.inport;

import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AbstractCommonRequestInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationConfirmCommand extends AbstractCommonRequestInfo {
	private Long temporaryReservationId;

	private Long concertOptionId; // concertoptionId와 userId만 있어도 되지 않을까?
	private Long userId;
}
