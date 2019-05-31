package com.smarttrafficprototype.trafficmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.smarttrafficprototype.trafficmanager.Setup;
import com.smarttrafficprototype.trafficmanager.service.registration.TrafficJunction;

@Component
public class SetupService {

	private RestTemplate restTemplate;

	@Autowired
	private ServiceRegistry serviceRegistry;

	@Autowired
	private Setup setup;

	@Value("${request.timeout}")
	private int timeout;
	
	public SetupService(RestTemplateBuilder rtBuilder) {
		this.restTemplate = rtBuilder.build();
	}
	

	public void initializeSetup() {
		configureRestTemplateForSetup();

		for (int i = 0; i < serviceRegistry.getTrafficJunctions().size(); i++ ) {
			if (i == serviceRegistry.getIdCurrentJunction()) {
				continue;
			}
			TrafficJunction trafficJunction = serviceRegistry.getTrafficJunctions().get(i);
			restTemplate.getForObject(new StringBuilder("http://").append(trafficJunction.getHost()).append(":")
					.append(trafficJunction.getPort()).append("/trafficManager/setuped").toString(), String.class);
		}
		
		
		configureRestTemplateForCalls();
		setup.activate();
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
