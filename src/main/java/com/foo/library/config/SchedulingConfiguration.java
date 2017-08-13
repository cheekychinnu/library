package com.foo.library.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
@Configuration
public class SchedulingConfiguration {
	
	@Bean
	public DueDateReminder getDueDateReminder() {
		return new DueDateReminder();
	}
}
