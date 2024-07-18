package io.messageservice.common.shedlock;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shedlock")
public class ShedLock {
	@Id
	@Column(name = "name", length = 120, nullable = false, unique = true)
	private String name;

	@Column(name = "lock_until", columnDefinition = "TIMESTAMP(3)", nullable = true)
	private LocalDateTime lockUntil;

	@Column(name = "locked_at", columnDefinition = "TIMESTAMP(3)", nullable = true)
	private LocalDateTime lockedAt;

	@Column(name = "locked_by", length = 255, nullable = false)
	private String lockedBy;
}
