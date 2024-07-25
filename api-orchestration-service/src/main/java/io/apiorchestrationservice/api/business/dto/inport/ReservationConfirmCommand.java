package io.apiorchestrationservice.api.business.dto.inport;

import io.apiorchestrationservice.api.application.dto.request.PaymentProcessRequest;
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
	private Long temporalReservationId;

	private Long concertOptionId; // concertoptionId와 userId만 있어도 되지 않을까?
	private Long userId;

	public static ReservationConfirmCommand createConfirmCommand(PaymentProcessRequest request) {
		return ReservationConfirmCommand.builder().temporalReservationId(Long.valueOf(request.getTargetId())).build();
	}
}
