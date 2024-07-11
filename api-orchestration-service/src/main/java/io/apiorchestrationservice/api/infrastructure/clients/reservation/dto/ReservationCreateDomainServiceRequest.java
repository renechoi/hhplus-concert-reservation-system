package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import io.apiorchestrationservice.api.business.dto.inport.TemporaryReservationCreateCommand;
import io.apiorchestrationservice.common.mapper.ObjectMapperBasedVoMapper;
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
public class ReservationCreateDomainServiceRequest extends AbstractCommonRequestInfo {

	private Long concertOptionId;
	private Long seatNumber;
	private Long userId;

	public static ReservationCreateDomainServiceRequest from(TemporaryReservationCreateCommand command) {
		return ObjectMapperBasedVoMapper.convert(command, ReservationCreateDomainServiceRequest.class);
	}
}
