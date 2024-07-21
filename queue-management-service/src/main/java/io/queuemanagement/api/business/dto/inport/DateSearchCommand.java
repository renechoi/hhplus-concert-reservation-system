package io.queuemanagement.api.business.dto.inport;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/22
 */
public interface DateSearchCommand {

	@Getter
	@RequiredArgsConstructor
	enum DateSearchCondition {
		BEFORE("이전", "지정된 날짜 이전"),
		AFTER("이후", "지정된 날짜 이후"),
		ON("당일", "지정된 날짜 당일");

		private final String title;
		private final String description;
	}

	@Getter
	@RequiredArgsConstructor
	enum DateSearchTarget {
		VALID_UNTIL("유효 시각", "토큰의 유효 시각"),
		CREATED_AT("생성 시각", "토큰이 생성된 시각");

		private final String title;
		private final String description;
	}
}
