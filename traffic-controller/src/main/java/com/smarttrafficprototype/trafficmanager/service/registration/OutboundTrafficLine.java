package com.smarttrafficprototype.trafficmanager.service.registration;

import com.smarttrafficprototype.trafficmanager.service.SensingUnit;

public interface OutboundTrafficLine {

	TrafficJunction getOutboundTrafficJunction();
	SensingUnit getSensingUnit();

}
