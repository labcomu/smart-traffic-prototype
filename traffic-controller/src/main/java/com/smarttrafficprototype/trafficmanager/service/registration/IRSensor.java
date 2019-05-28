package com.smarttrafficprototype.trafficmanager.service.registration;

import java.util.Random;

public class IRSensor implements Sensor {
	
	private SensingUnitObserver sensingObserver;
	
	public IRSensor() {
		registerSensingUnit(sensingObserver);
	}

	@Override
	public void registerSensingUnit(SensingUnitObserver sensingObserver) {
		this.sensingObserver = sensingObserver;
	}

	@Override
	public void notifySensingUnit() {
		Integer countCars = new Random().nextInt(2);
		sensingObserver.increaseDensity(countCars);
	}

}
