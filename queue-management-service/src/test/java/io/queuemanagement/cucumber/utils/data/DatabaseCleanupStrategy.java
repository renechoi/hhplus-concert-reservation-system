package io.queuemanagement.cucumber.utils.data;

import java.util.List;

import jakarta.persistence.EntityManager;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */

public interface DatabaseCleanupStrategy {
	void disableConstraints(EntityManager entityManager);
	void truncateTables(EntityManager entityManager, List<String> tableNames);
	void enableConstraints(EntityManager entityManager);
}
