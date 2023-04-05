package com.quickpik.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Creates and returns a new instance of ModelMapper.
 * @return a new instance of ModelMapper
 */

@Configuration
public class ProjectConfig {
	@Bean
	ModelMapper mapper() {
		return new ModelMapper();
	}
}
