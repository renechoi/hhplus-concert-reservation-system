package io.reservationservice.api.application.dto.request;

import io.reservationservice.api.business.dto.common.AbstractCommonRequestInfo;
import io.reservationservice.api.business.dto.inport.ConcertCreateCommand;
import io.reservationservice.common.mapper.ObjectMapperBasedVoMapper;
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