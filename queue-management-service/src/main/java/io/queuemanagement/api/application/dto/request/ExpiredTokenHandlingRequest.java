package io.queuemanagement.api.application.dto.request;

import java.util.List;

import io.queuemanagement.api.business.dto.inport.ExpiredTokenHandlingCommand;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/08/01
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpiredTokenHandlingRequest {
	private List<String> keys;
	private int size;

	public static ExpiredTokenHandlingRequest expireCommand(List<String> keys) {
		return ExpiredTokenHandlingRequest.builder()
			.keys(keys)
			.size(keys.size())
			.build();
	}

	public ExpiredTokenHandlingCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, ExpiredTokenHandlingCommand.class);
	}
}
