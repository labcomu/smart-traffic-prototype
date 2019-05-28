package com.smarttrafficprototype.trafficmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.microthingsexperiment.ActiveProfiles;
import com.smarttrafficprototype.trafficmanager.Setup;
import com.smarttrafficprototype.trafficmanager.service.registration.TrafficJunction;

@Component
public class SetupService {

	private RestTemplate restTemplate;

	@Autowired
	private ServiceRegistry serviceRegistry;

	@Autowired
	private Setup setup;
	@Autowired
	private ActiveProfiles profiles;

	@Value("${timeout:1000}")
	private int timeout;
	
	public SetupService(RestTemplateBuilder rtBuilder) {
		this.restTemplate = rtBuilder.build();
	}
	

	public void initializeSetup() {
		setup.activate();
		
		configureRestTemplateForSetup();


		if (profiles.isProfileActive("gatewayRequest")) {
			restTemplate.getForObject(
					new StringBuilder("http://").append(serviceRegistry.getGateway().getHost()).append(":")
							.append(serviceRegistry.getGateway().getPort()).append("/gateway/setup").toString(),
					String.class);
		}

		for (TrafficJunction device : serviceRegistry.getTrafficJunctions()) {
			restTemplate.getForObject(new StringBuilder("http://").append(device.getHost()).append(":")
					.append(device.getPort()).append("/device/setup").toString(), String.class);
		}
		
		
		configureRestTemplateForCalls();
	}


	private void configureRestTemplateForSetup() {
		SimpleClientHttpRequestFactory  rf = (SimpleClientHttpRequestFactory ) restTemplate
				.getRequestFactory();

		rf.setReadTimeout(10000);
		rf.setConnectTimeout(10000);
	}
	
	private void configureRestTemplateForCalls() {
		SimpleClientHttpRequestFactory  rf = (SimpleClientHttpRequestFactory ) restTemplate
				.getRequestFactory();

		rf.setReadTimeout(timeout);
		rf.setConnectTimeout(timeout);
		
	}

}
