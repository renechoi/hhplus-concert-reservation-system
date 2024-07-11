package io.apiorchestrationservice.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */
@Slf4j
public class CustomFieldMapper {

	private static final Random random = new Random();

	public static <T> T applyCustomMappings(T targetObject, Map<String, String> fieldValues) {
		for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
			try {
				Field field = targetObject.getClass().getDeclaredField(entry.getKey());
				field.setAccessible(true);

				if ("now".equalsIgnoreCase(entry.getValue())) {
					if (field.getType().equals(LocalDateTime.class)) {
						field.set(targetObject, LocalDateTime.now());
					} else if (field.getType().equals(LocalDate.class)) {
						field.set(targetObject, LocalDate.now());
					}
				} else if ("random".equalsIgnoreCase(entry.getValue())) {
					setRandomValue(field, targetObject);
				} else if ("randomAfter".equalsIgnoreCase(entry.getValue())) {
					setRandomAfterValue(field, targetObject);
				} else {
					field.set(targetObject, entry.getValue());
				}
			} catch (Exception e) {
				log.info("필드를 찾을 수 없거나 설정할 수 없습니다: " + entry.getKey() + ", 건너뜁니다.", e);
			}
		}
		return targetObject;
	}

	private static void setRandomValue(Field field, Object targetObject) throws IllegalAccessException {
		if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
			field.set(targetObject, random.nextLong());
		} else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
			field.set(targetObject, random.nextInt());
		} else if (field.getType().equals(String.class)) {
			field.set(targetObject, UUID.randomUUID().toString());
		} else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
			field.set(targetObject, random.nextDouble());
		} else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
			field.set(targetObject, random.nextFloat());
		} else if (field.getType().equals(BigInteger.class)) {
			field.set(targetObject, BigInteger.valueOf(random.nextLong()));
		} else if (field.getType().equals(BigDecimal.class)) {
			field.set(targetObject, BigDecimal.valueOf(random.nextDouble()));
		} else if (field.getType().equals(Duration.class)) {
			field.set(targetObject, Duration.ofMillis(random.nextInt(1000 * 60 * 60 * 24))); // 최대 24시간까지 임의의 지속 시간
		} else if (field.getType().equals(LocalDate.class)) {
			field.set(targetObject, LocalDate.ofEpochDay(random.nextInt(365 * 100))); // 100년 이내의 임의의 날짜
		} else if (field.getType().equals(LocalDateTime.class)) {
			field.set(targetObject, LocalDateTime.ofEpochSecond(random.nextInt(1000 * 60 * 60 * 24 * 365), 0, java.time.ZoneOffset.UTC)); // 100년 이내의 임의의 날짜 및 시간
		}
	}

	private static void setRandomAfterValue(Field field, Object targetObject) throws IllegalAccessException {
		if (field.getType().equals(LocalDate.class)) {
			field.set(targetObject, LocalDate.now().plusDays(random.nextInt(365 * 100))); // 오늘 이후의 100년 이내의 임의의 날짜
		} else if (field.getType().equals(LocalDateTime.class)) {
			field.set(targetObject, LocalDateTime.now().plusSeconds(random.nextInt(1000 * 60 * 60 * 24 * 365))); // 현재 이후의 100년 이내의 임의의 날짜 및 시간
		}
	}
}
