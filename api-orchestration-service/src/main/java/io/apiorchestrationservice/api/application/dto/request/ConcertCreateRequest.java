package io.apiorchestrationservice.api.application.dto.request;

import io.apiorchestrationservice.api.business.dto.inport.ConcertCreateCommand;
import io.apiorchestrationservice.api.infrastructure.clients.reservation.dto.AbstractCommonRequestInfo;
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
public class ConcertCreateRequest extends AbstractCommonRequestInfo {
	private String title;

	public ConcertCreateCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, ConcertCreateCommand.class);
	}
}