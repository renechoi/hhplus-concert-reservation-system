package io.queuemanagement.api.business.service;

import org.springframework.stereotype.Service;

import io.queuemanagement.api.business.domainmodel.WaitingQueueToken;
import io.queuemanagement.api.business.dto.inport.TokenGenerateCommand;
import io.queuemanagement.api.business.persistence.TokenStoreRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Service
@RequiredArgsConstructor
public class SimpleTokenService implements TokenService {
	private final TokenStoreRepository tokenStoreRepository;

	@Override
	public WaitingQueueToken generateToken(TokenGenerateCommand tokenGenerateCommand) {
		return null;
	}
}
