package com.smarttrafficprototype.trafficmanager.service.registration;

import com.smarttrafficprototype.trafficmanager.service.SensingUnit;

public interface InboundTrafficLine {

	TrafficJunction getInboundTrafficJunction();
	TrafficLight getTrafficLight();
	SensingUnit getSensingUnit();
	Integer getTotalDensity();
	void setIncomingDensity(Integer incomingDensity);

}
