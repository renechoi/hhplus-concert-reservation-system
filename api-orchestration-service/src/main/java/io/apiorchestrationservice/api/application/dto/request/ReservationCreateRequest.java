package io.apiorchestrationservice.api.application.dto.request;

import io.apiorchestrationservice.api.business.dto.inport.TemporalReservationCreateCommand;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AbstractCommonRequestInfo;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationCreateRequest extends AbstractCommonRequestInfo {
	private Long userId;
	private Long concertOptionId;
	private Long seatNumber;

	public TemporalReservationCreateCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, TemporalReservationCreateCommand.class);
	}
}
