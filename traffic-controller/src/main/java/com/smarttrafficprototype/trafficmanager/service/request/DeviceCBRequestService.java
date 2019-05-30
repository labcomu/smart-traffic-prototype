package com.smarttrafficprototype.trafficmanager.service.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.microthingsexperiment.circuitbreaker.CircuitBreakerManager;
import com.microthingsexperiment.circuitbreaker.ResponseWrapper;

@Component
@Profile("deviceCBRequest")
public class DeviceCBRequestService implements RemoteRequestService {

	@Autowired
	private CircuitBreakerManager<Integer> cbService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@SuppressWarnings("unchecked")
	@Override
	public Integer requestDensity(String deviceHost, String devicePort, String[] values) {
		String deviceId = deviceHost + ":" + devicePort;

		Integer result = Integer.MIN_VALUE;

		StringBuilder urlBuilder = new StringBuilder("http://")
				.append(deviceHost)
				.append(":")
				.append(devicePort)
				.append("/trafficManager");
		
		for (String value : values) {
			urlBuilder.append("/").append(value);
		}
		
		String baseUrl = urlBuilder.toString();

		try {
			logger.info("Request Started: " + baseUrl);

			 ResponseWrapper<Integer> response = cbService.executeGetRequest(baseUrl, deviceId, Integer.class);
			result = response.getResponse();
			
			logger.info("Request Returned: " + baseUrl);

		} catch (Exception ex) {
			logger.info("Failure Requesting: " + baseUrl);
			throw ex;
		}
		return result;
	}

}
