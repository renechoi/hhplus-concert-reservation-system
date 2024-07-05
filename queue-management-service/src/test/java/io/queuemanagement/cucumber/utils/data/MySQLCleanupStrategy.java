package io.queuemanagement.cucumber.utils.data;

import java.util.List;

import jakarta.persistence.EntityManager;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */

public class MySQLCleanupStrategy implements DatabaseCleanupStrategy {
	@Override
	public void disableConstraints(EntityManager entityManager) {
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=0").executeUpdate();
	}

	@Override
	public void truncateTables(EntityManager entityManager, List<String> tableNames) {
		tableNames.forEach(tableName ->
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate());
	}

	@Override
	public void enableConstraints(EntityManager entityManager) {
		entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=1").executeUpdate();
	}
}
