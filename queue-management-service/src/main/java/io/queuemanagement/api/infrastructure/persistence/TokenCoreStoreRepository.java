package io.queuemanagement.api.infrastructure.persistence;

import org.springframework.stereotype.Repository;

import io.queuemanagement.api.business.persistence.TokenStoreRepository;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/04
 */
@Repository
@RequiredArgsConstructor
public class TokenCoreStoreRepository implements TokenStoreRepository {
	private final WaitingQueueTokenJpaRepository waitingQueueTokenJpaRepository;
}
