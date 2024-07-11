package io.paymentservice.cucumber.util.data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.metamodel.EntityType;

/**
 * @author : Rene Choi
 * @since : 2024/06/23
 */

@Component
public class DatabaseCleanupExecutor implements InitializingBean {

	@PersistenceContext
	private EntityManager entityManager;

	private List<String> tableNames;

	private DatabaseCleanupStrategy cleanupStrategy;

	@Autowired
	private Environment env;

	@Override
	public void afterPropertiesSet() {
		decideDatabase();

		parseTableNames();
	}

	private void parseTableNames() {
		tableNames = entityManager.getMetamodel().getEntities().stream()
			.filter(DatabaseCleanupExecutor::isEntityFound)
			.map(entityType -> convertToSnakeCase(entityType.getName()))
			.collect(Collectors.toList());

	}

	private void decideDatabase() {
		String databaseType = env.getProperty("spring.datasource.driver-class-name");
		if (databaseType != null) {
			if (databaseType.contains("h2")) {
				cleanupStrategy = new H2CleanupStrategy();
			} else if (databaseType.contains("mysql")) {
				cleanupStrategy = new MySQLCleanupStrategy();
			}
		}
	}

	@Transactional
	public void execute() {
		entityManager.flush();
		cleanupStrategy.disableConstraints(entityManager);
		cleanupStrategy.truncateTables(entityManager, tableNames);
		cleanupStrategy.enableConstraints(entityManager);
	}

	private static boolean isEntityFound(EntityType<?> entity) {
		return entity.getJavaType().getAnnotation(Entity.class) != null;
	}

	private String convertToSnakeCase(String tableName) {
		return IntStream.range(0, tableName.length())
			.mapToObj(i -> {
				char c = tableName.charAt(i);
				return Character.isUpperCase(c) ?
					(i > 0 ? "_" : "") + Character.toLowerCase(c) :
					String.valueOf(c);
			})
			.collect(Collectors.joining());
	}

}