package com.smarttrafficprototype.trafficmanager.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smarttrafficprototype.trafficmanager.service.registration.OutboundTrafficLine;
import com.smarttrafficprototype.trafficmanager.service.registration.TrafficJunction;

@Service
public class TrafficJunctionService {
	
	@Autowired
	private TrafficJunction trafficJunction;
	
	public Logger logger = LoggerFactory.getLogger(getClass());

	public Integer getOutboundDensityByTrafficJuncion(String junctionKey) {
		Optional<OutboundTrafficLine> trafficLine = Optional.ofNullable(null);
		
		for (OutboundTrafficLine outboundTrafficLine : trafficJunction.getOutboundLines()) {
			if (outboundTrafficLine.getOutboundTrafficJunction() != null 
					&& outboundTrafficLine.getOutboundTrafficJunction().getJunctionKey().equals(junctionKey)) {
				trafficLine = Optional.of(outboundTrafficLine);
				break;
			}
		}
		OutboundTrafficLine outbound = trafficLine.orElseThrow(() -> new RuntimeException("There is no Traffic Line for the informed Key"));
		
		Integer density = outbound.getSensingUnit().getResultDensity();
		return density;
	}
	
	
}
