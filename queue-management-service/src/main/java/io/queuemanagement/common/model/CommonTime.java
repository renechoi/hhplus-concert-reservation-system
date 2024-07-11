package io.queuemanagement.common.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author : Rene Choi
 * @since : 2024/07/07
 */

@Getter
@RequiredArgsConstructor
public enum CommonTime {
	ONE_SECOND("1s", 1_000L),
	TEN_SECONDS("10s", 10_000L),
	FIFTEEN_SECONDS("15s", 15_000L),
	THIRTY_SECONDS("30s", 30_000L),
	SIXTY_SECONDS("60s", 60_000L),
	ONE_MINUTE("1m", 60_000L),
	FIVE_MINUTES("5m", 300_000L),
	TEN_MINUTES("10m", 600_000L),
	THIRTY_MINUTES("30m", 1_800_000L),
	ONE_HOUR("1h", 3_600_000L),
	ONE_DAY("1d", 86_400_000L);

	private final String timeLabel;
	private final long milliseconds;

	public static long commonTime(String timeString) {
		for (CommonTime time : values()) {
			if (time.getTimeLabel().equalsIgnoreCase(timeString)) {
				return time.getMilliseconds();
			}
		}
		return parseTimeStringToMillis(timeString);
	}

	private static long parseTimeStringToMillis(String timeString) {
		try {
			long time = Long.parseLong(timeString.replaceAll("[^0-9]", ""));
			if (timeString.contains("s")) {
				return time * 1000;
			} else if (timeString.contains("m")) {
				return time * 60_000;
			} else if (timeString.contains("h")) {
				return time * 3_600_000;
			} else if (timeString.contains("d")) {
				return time * 86_400_000;
			}
		} catch (NumberFormatException e) {
		}
		return -1;
	}

	@Override
	public String toString() {
		return "CommonTime{ milliseconds=" + milliseconds + '}';
	}

}