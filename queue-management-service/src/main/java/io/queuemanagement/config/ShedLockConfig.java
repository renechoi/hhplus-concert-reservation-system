package io.queuemanagement.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;

/**
 * @author : Rene Choi
 * @since : 2024/07/11
 */

@Configuration
public class ShedLockConfig {

	@Bean
	public JdbcTemplateLockProvider lockProvider(DataSource dataSource) {

		return new JdbcTemplateLockProvider(
			JdbcTemplateLockProvider.Configuration.builder()
				.withJdbcTemplate(new JdbcTemplate(dataSource))
				.usingDbTime()
				.build()
		);
	}
}
