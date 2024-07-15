package io.queuemanagement.cucumber.steps;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import io.cucumber.java.After;
import io.cucumber.java8.En;
import io.queuemanagement.util.YmlLoader;

/**
 * Author: Rene Choi
 * Since: 2024/07/07
 */
@ExtendWith(MockitoExtension.class)
@ContextConfiguration
public class ConfigYmlMockStepDef implements En {

	private Map<String, String > originalConfigMap;

	public ConfigYmlMockStepDef() {
		And("yml loader가 waiting-queue.policy.max-limit 설정을 {int}로 리턴하도록 mocking한다", this::mockYmlLoaderWaitingQueueMaxLimit);
		And("yml loader가 processing-queue.policy.max-limit 설정을 {int}로 리턴하도록 mocking한다", this::mockYmlLoaderProcessingQueueMaxLimit);
	}


	private void mockYmlLoaderWaitingQueueMaxLimit(Integer maxLimit) {
		Map<String, String > configMap = YmlLoader.getConfigMap();

		if (originalConfigMap == null) {
			originalConfigMap = new HashMap<>(configMap);
		}

		configMap.put("waiting-queue.policy.max-limit", String.valueOf(maxLimit));
	}

	private void mockYmlLoaderProcessingQueueMaxLimit(Integer maxLimit) {
		Map<String, String > configMap = YmlLoader.getConfigMap();

		if (originalConfigMap == null) {
			originalConfigMap = new HashMap<>(configMap);
		}

		configMap.put("processing-queue.policy.max-limit", String.valueOf(maxLimit));
	}

	@After("@YmlLoaderConfig")
	public void tearDown() {
		if (originalConfigMap != null) {
			Map<String, String > configMap = YmlLoader.getConfigMap();

			configMap.put("processing-queue.policy.max-limit", originalConfigMap.get("processing-queue.policy.max-limit"));

			originalConfigMap = null;
		}
	}
}