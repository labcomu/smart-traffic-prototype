package com.smarttrafficprototype.trafficmanager;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {
	
	@Bean
	public RestTemplateBuilder restTemplateBuilder(@Value("${request.timeout}")long timeout) {
		return new RestTemplateBuilder().setConnectTimeout(Duration.ofMillis(timeout))
				.setReadTimeout(Duration.ofMillis(timeout));
	}
	
	
}
