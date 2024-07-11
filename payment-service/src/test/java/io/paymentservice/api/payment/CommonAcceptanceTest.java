package io.paymentservice.api.payment;

import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import io.paymentservice.cucumber.util.data.DatabaseCleanupExecutor;
import io.paymentservice.testhelpers.apiexecutor.DynamicPortHolder;
import io.paymentservice.testhelpers.contextholder.TestDtoContextHolder;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommonAcceptanceTest {

	@LocalServerPort
	private int port;

	@Autowired
	private DatabaseCleanupExecutor databaseCleanupExecutor;

	@Autowired
	private ApplicationContext applicationContext;

	@DynamicPropertySource
	static void registerWireMockProperties(DynamicPropertyRegistry registry) {
	}

	@BeforeEach
	public void setUp() {
		setDynamicPort(port);
		databaseCleanup();
	}

	@AfterEach
	public void after(){
		initializeAllContextHolders();
	}

	private void setDynamicPort(int port) {
		DynamicPortHolder.setPort(port);
	}

	public void databaseCleanup() {
		databaseCleanupExecutor.execute();
		initializeAllContextHolders();
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