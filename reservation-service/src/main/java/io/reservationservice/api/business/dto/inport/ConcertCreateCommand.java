package io.reservationservice.api.business.dto.inport;

import io.reservationservice.api.business.domainentity.Concert;
import io.reservationservice.api.business.dto.common.AbstractCommonRequestInfo;
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
public class ConcertCreateCommand extends AbstractCommonRequestInfo {
	private String title;

	public Concert toEntity() {
		return ObjectMapperBasedVoMapper.convert(this, Concert.class);
	}
}
