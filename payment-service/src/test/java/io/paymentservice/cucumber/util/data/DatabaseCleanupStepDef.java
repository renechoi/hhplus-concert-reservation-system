package io.paymentservice.cucumber.util.data;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import io.cucumber.java8.En;
import io.paymentservice.testhelpers.contextholder.TestDtoContextHolder;

/**
 * @author : Rene Choi
 * @since : 2024/06/17
 */
public class DatabaseCleanupStepDef implements En {

	@Autowired
	private DatabaseCleanupExecutor databaseCleanupExecutor;


	@Autowired
	private ApplicationContext applicationContext;

	public DatabaseCleanupStepDef() {
		Before(() -> {
			databaseCleanupExecutor.execute();
			initializeAllContextHolders();
		});
	}

	private void initializeAllContextHolders() {
		Map<String, TestDtoContextHolder> beansOfType = applicationContext.getBeansOfType(TestDtoContextHolder.class);
		for (TestDtoContextHolder contextHolder : beansOfType.values()) {
			try {
				Method initFieldsMethod = contextHolder.getClass().getMethod("initFields");
				initFieldsMethod.invoke(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
