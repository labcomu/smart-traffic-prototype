package com.smarttrafficprototype.trafficmanager.service.registration;

public interface Sensor {
	
	void registerSensingUnit(SensingUnitObserver sensingObserver);
	void notifySensingUnit();

}
