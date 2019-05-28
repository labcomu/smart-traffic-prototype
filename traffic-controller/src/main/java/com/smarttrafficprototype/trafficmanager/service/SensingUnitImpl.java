package com.smarttrafficprototype.trafficmanager.service;

import java.util.ArrayList;
import java.util.List;

import com.smarttrafficprototype.trafficmanager.service.registration.SensingUnitObserver;
import com.smarttrafficprototype.trafficmanager.service.registration.Sensor;

public class SensingUnitImpl implements SensingUnit, SensingUnitObserver {
	
	private Integer density;
	private List<Sensor> sensorArray = new ArrayList<>();
	
	public SensingUnitImpl(List<Sensor> sensors) {
		clearDensity();
		this.sensorArray = sensors;
	}

	@Override
	public Integer getResultDensity() {
		return this.density;
	}

	@Override
	public void increaseDensity(Integer countCars) {
		this.density += countCars;
	}

	@Override
	public void clearDensity() {
		this.density = 0;
	}


}
