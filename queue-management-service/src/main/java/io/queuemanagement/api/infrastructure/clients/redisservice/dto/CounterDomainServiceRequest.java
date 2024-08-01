package io.queuemanagement.api.infrastructure.clients.redisservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CounterDomainServiceRequest {
	private String counterKey;
	private long value;

	public static CounterDomainServiceRequest incrementRequest(String counterKey, long value){
		return CounterDomainServiceRequest.builder().counterKey(counterKey).value(value).build();
	}

	public static CounterDomainServiceRequest decrementRequest(String counterKey, long value){
		return CounterDomainServiceRequest.builder().counterKey(counterKey).value(value).build();
	}
}