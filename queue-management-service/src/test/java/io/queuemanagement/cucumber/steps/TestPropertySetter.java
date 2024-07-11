package io.queuemanagement.cucumber.steps;

import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.test.context.support.TestPropertySourceUtils;

@Component
public class TestPropertySetter {

    private final Environment environment;

    public TestPropertySetter(Environment environment) {
        this.environment = environment;
    }

    public void setProperty(String key, String value) {
        String property = key + "=" + value;
        if (environment instanceof StandardEnvironment) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                (StandardEnvironment) environment, property);
        } else {
            throw new IllegalArgumentException("Unsupported environment type: " + environment.getClass());
        }
    }
}