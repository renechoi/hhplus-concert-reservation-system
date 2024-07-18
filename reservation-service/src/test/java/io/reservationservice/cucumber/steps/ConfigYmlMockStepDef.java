package io.reservationservice.cucumber.steps;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import io.cucumber.java.After;
import io.cucumber.java8.En;
import io.reservationservice.util.YmlLoader;

/**
 * Author: Rene Choi
 * Since: 2024/07/07
 */
@ExtendWith(MockitoExtension.class)
@ContextConfiguration
public class ConfigYmlMockStepDef implements En {

	private Map<String, String > originalConfigMap;

	public ConfigYmlMockStepDef() {
		And("yml loader가 reservation.temporary.expire-seconds 설정을 {int}로 리턴하도록 mocking한다", this::mockYmlLoaderTemporaryExpireSeconds);
	}


	private void mockYmlLoaderTemporaryExpireSeconds(Integer expireValue) {
		Map<String, String > configMap = YmlLoader.getConfigMap();

		if (originalConfigMap == null) {
			originalConfigMap = new HashMap<>(configMap);
		}

		configMap.put("reservation.temporary.expire-seconds", String.valueOf(expireValue));
	}

	@After("@YmlLoaderConfig")
	public void tearDown() {
		if (originalConfigMap != null) {
			Map<String, String > configMap = YmlLoader.getConfigMap();

			configMap.put("reservation.temporary.expire-seconds", originalConfigMap.get("reservation.temporary.expire-seconds"));


			originalConfigMap = null;
		}
	}
}