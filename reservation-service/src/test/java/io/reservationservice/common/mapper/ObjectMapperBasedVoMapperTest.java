package io.reservationservice.common.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.reservationservice.api.business.dto.outport.AvailableDateInfo;
import io.reservationservice.api.business.dto.outport.AvailableDateInfos;
import io.reservationservice.api.business.dto.outport.CacheInfo;

/**
 * @author : Rene Choi
 * @since : 2024/07/30
 */

public class ObjectMapperBasedVoMapperTest {

	private static ObjectMapper objectMapper;

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Test
	@DisplayName("일급 컬렉션 객체로 변환 테스트")
	public void testConvertForCollectionObject() throws Exception {
		// given
		String cacheKey = "availableDates-1";
		String cacheValueJson = "{\"available_date_infos\":[{\"concert_option_id\":1,\"concert_date\":\"2026-07-10T20:00:00\",\"concert_duration\":7200.0,\"title\":\"옵션 제목\",\"description\":\"옵션 설명\",\"price\":10000.0}]}";
		JsonNode cacheValue = objectMapper.readTree(cacheValueJson);
		CacheInfo cacheInfo = new CacheInfo(cacheKey, cacheValue, null, true);

		// when
		AvailableDateInfos result = ObjectMapperBasedVoMapper.convertForCollectionObject(cacheInfo.cacheValue(), AvailableDateInfos.class);

		// then
		List<AvailableDateInfo> expectedAvailableDateInfos = List.of(new AvailableDateInfo(1L, LocalDateTime.of(2026, 7, 10, 20, 0), Duration.ofHours(2), "옵션 제목", "옵션 설명", BigDecimal.valueOf(10000.0)));
		AvailableDateInfos expected = new AvailableDateInfos(expectedAvailableDateInfos);

		assertEquals(expected, result);
	}
}