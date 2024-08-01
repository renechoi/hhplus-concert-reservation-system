package io.queuemanagement.api.application.dto.request;

import java.util.List;

import io.queuemanagement.api.business.dto.inport.CompletedTokenHandlingCommand;
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
public class CompletedTokenHandlingRequest {
	private String userId;



	public CompletedTokenHandlingCommand toCommand() {
		return ObjectMapperBasedVoMapper.convert(this, CompletedTokenHandlingCommand.class);
	}
}
