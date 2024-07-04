package io.queuemanagement.api.infrastructure.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import io.queuemanagement.api.business.domainmodel.WaitingQueueStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/03
 */


@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WaitingQueueTokenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long waitingQueueTokenId;
	private String userId;
	private String tokenValue;
	private LocalDateTime remainingTime;
	private int position;
	private LocalDateTime validUntil;
	private WaitingQueueStatus status;


	public static WaitingQueueTokenEntity createMockData(String userId) {
		return WaitingQueueTokenEntity.builder()
			.waitingQueueTokenId(1L) // 임의의 ID, 실제로는 DB에서 생성됨
			.userId(userId)
			.tokenValue(UUID.randomUUID().toString())
			.remainingTime(LocalDateTime.now().plusMinutes(30)) // 30분 대기 시간 설정
			.position(1) // 임의의 대기 순서
			.validUntil(LocalDateTime.now().plusHours(1)) // 1시간 유효 기간 설정
			.status(WaitingQueueStatus.WAITING) // 초기 상태는 '대기'
			.build();
	}
}
