package io.queuemanagement.api.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import io.queuemanagement.cucumber.utils.data.DatabaseCleanupExecutor;

/**
 * @author : Rene Choi
 * @since : 2024/07/17
 */
@Component
@ActiveProfiles("test")
public class CommonAcceptanceTest {
	@Autowired
	private DatabaseCleanupExecutor databaseCleanupExecutor;

	@BeforeEach
	public void databaseCleanup() {
		databaseCleanupExecutor.execute();
	}
}
