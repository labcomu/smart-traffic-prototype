package com.smarttrafficprototype.trafficmanager.service.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Profile("gatewayRequest")
public class GatewayRequestService implements RemoteRequestService {

	private RestTemplate restTemplate;
	@Value("${setup.gateway.host}")
	private String gatewayHost;
	@Value("${setup.gateway.port}")
	private String gatewayPort;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public GatewayRequestService(RestTemplateBuilder rtBuilder) {
		this.restTemplate = rtBuilder.build();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer requestDensity(String host, String port, String[] values) {
		
		Integer result = Integer.MIN_VALUE;
		
		String baseUrl = new StringBuilder("http://")
				.append(gatewayHost)
				.append(":")
				.append(gatewayPort)
				.append("/gateway").toString();
		

		try {	
			HttpHeaders headers = new HttpHeaders();
			
			headers.add("device-host", host);
			headers.add("device-port", port);
			
			HttpEntity<HttpHeaders> httpEntity = new HttpEntity<>(headers);
			
			logger.info("Request Started: "+baseUrl+"->["+host+":"+port+"]");

			ResponseEntity<Integer> response = restTemplate.exchange(baseUrl, HttpMethod.GET, httpEntity, Integer.class);
			
			result = response.getBody();
			
			logger.info("Request Returned: "+baseUrl+"->["+host+":"+port+"]");
			
		} catch (Exception ex) {
			logger.info("Failure Requesting: "+baseUrl);
			throw new RuntimeException(ex);

		}
		
		return result;
	}
}
