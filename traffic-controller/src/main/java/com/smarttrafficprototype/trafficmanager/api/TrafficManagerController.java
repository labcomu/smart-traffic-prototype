package com.smarttrafficprototype.trafficmanager.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smarttrafficprototype.trafficmanager.service.SetupService;
import com.smarttrafficprototype.trafficmanager.service.TrafficJunctionService;

@RestController
@RequestMapping("/trafficManager")
public class TrafficManagerController {
	
	@Autowired
	private TrafficJunctionService service;
	
	@Autowired
	private SetupService setupService;
	
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/{idJunction}")
	public ResponseEntity<Integer> call(@PathVariable String idJunction) {
		
		ResponseEntity<Integer> response;
		
		try {
			logger.info("Starting:"+"TrafficManager.call() to " + idJunction);

			
			Integer densityValue = service.getOutboundDensityByTrafficJuncion(idJunction);
			
			logger.info("Returning:"+"TrafficManager.call():"+densityValue);

			response = new ResponseEntity<>(densityValue, HttpStatus.OK);
			
		} catch (Exception e) {
			
			logger.info("Failure:"+"TrafficManager.call()");
			logger.error("Failure to TrafficManager.call()",e);
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("error-message", e.getCause().toString());
			
			response = new ResponseEntity<>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
		
	}
	
	
	@GetMapping("/setup")
	public ResponseEntity<String> setup() {
		
		logger.info("Starting Caller.Setup:[]");
		setupService.initializeSetup();
		
		logger.info("Finishing Caller.Setup:[]");
		
		return new ResponseEntity<>("SETUP OK", HttpStatus.OK);
	}

}