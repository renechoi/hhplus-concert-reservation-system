package io.redisservice.cucumber;

import static io.cucumber.junit.platform.engine.Constants.*;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import io.cucumber.spring.CucumberContextConfiguration;
import io.redisservice.testhelpers.apiexecutor.DynamicPortHolder;

/**
 * @author : Rene Choi
 * @since : 2024/06/16
 */
@ActiveProfiles("test")
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("api-features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "io")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiCucumberTest {

	@LocalServerPort
	private void setDynamicPort(int port){
		DynamicPortHolder.setPort(port);
	}
}
