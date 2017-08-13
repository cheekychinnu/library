package com.foo.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@Configuration
public class SchedulingConfiguration {
	
	@Bean
	public DueDateReminder getDueDateReminder() {
		return new DueDateReminder();
	}
}
