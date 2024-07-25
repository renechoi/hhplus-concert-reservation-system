package io.apiorchestrationservice.api.application.dto.request;

import io.apiorchestrationservice.api.business.dto.inport.ReservationConfirmCommand;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AbstractCommonRequestInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
import jakarta.validation.constraints.NotNull;
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
public class ReservationConfirmRequest extends AbstractCommonRequestInfo {
	@NotNull
	private Long temporalReservationId;

	private Long concertOptionId; // concertoptionId와 userId만 있어도 되지 않을까?
	private Long userId;

	public ReservationConfirmCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, ReservationConfirmCommand.class);
	}
}