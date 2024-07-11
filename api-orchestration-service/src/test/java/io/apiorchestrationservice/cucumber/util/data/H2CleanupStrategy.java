package io.apiorchestrationservice.cucumber.util.data;

import java.util.List;

import jakarta.persistence.EntityManager;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */

public class H2CleanupStrategy implements DatabaseCleanupStrategy {
	@Override
	public void disableConstraints(EntityManager entityManager) {
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
	}

	@Override
	public void truncateTables(EntityManager entityManager, List<String> tableNames) {
		tableNames.forEach(tableName ->
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " RESTART IDENTITY").executeUpdate());
	}

	@Override
	public void enableConstraints(EntityManager entityManager) {
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
	}
}