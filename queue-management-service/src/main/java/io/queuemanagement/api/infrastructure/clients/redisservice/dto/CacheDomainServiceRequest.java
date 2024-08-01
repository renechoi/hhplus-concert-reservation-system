package io.queuemanagement.api.infrastructure.clients.redisservice.dto;

import static io.queuemanagement.util.YmlLoader.*;

import io.queuemanagement.api.business.domainmodel.ProcessingQueueToken;
import io.queuemanagement.api.business.dto.inport.CacheCommand;
import io.queuemanagement.common.mapper.ObjectMapperBasedVoMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheDomainServiceRequest {
	private String cacheKey;
	private Object cacheValue;
	private long ttl;

	public static CacheDomainServiceRequest from(CacheCommand command) {
		return ObjectMapperBasedVoMapper.convert(command, CacheDomainServiceRequest.class);
	}

	public static CacheDomainServiceRequest processingTokenQueue(ProcessingQueueToken processingQueueToken) {
		return CacheDomainServiceRequest.builder().cacheKey(processingQueueToken.getTokenValue()).cacheValue("NA").ttl(getProcessingTokenExpiryInSeconds()).build();
	}
}