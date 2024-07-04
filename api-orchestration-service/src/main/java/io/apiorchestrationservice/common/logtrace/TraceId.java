package io.apiorchestrationservice.common.logtrace;

import java.util.UUID;

import lombok.Getter;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */

@Getter
public class TraceId {

	private String id;
	private int level;

	public TraceId() {
		this.id = createId();
		this.level = 0;
	}

	private TraceId(String id, int level) {
		this.id = id;
		this.level = level;
	}

	private String createId() {
		return UUID.randomUUID().toString().substring(0, 8);
	}

	public TraceId createNextId() {
		return new TraceId(id, level + 1);
	}

	public TraceId createPreviousId() {
		return new TraceId(id, level - 1);
	}

	public boolean isFirstLevel() {
		return level == 0;
	}
}



