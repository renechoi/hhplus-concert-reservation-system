package io.apiorchestrationservice.api.infrastructure.clients.reservation.dto;

import io.apiorchestrationservice.api.business.dto.inport.ConcertCreateCommand;
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
public class ConcertCreateDomainServiceRequest extends AbstractCommonRequestInfo {
	private String title;

	public static ConcertCreateDomainServiceRequest from(ConcertCreateCommand command) {
		return ObjectMapperBasedVoMapper.convert(command, ConcertCreateDomainServiceRequest.class);
	}
}