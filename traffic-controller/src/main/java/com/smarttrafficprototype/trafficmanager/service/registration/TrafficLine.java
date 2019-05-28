package com.smarttrafficprototype.trafficmanager.service.registration;

import com.smarttrafficprototype.trafficmanager.service.SensingUnit;

public class TrafficLine implements InboundTrafficLine, OutboundTrafficLine {

	private SensingUnit sensingUnit;
	private TrafficJunction trafficJunction;
	private TrafficLight trafficLight;
	private Integer totalDensity = 0;
	
	public TrafficLine(SensingUnit sensingUnit, TrafficJunction trafficJunction, TrafficLight trafficLight) {
		this.sensingUnit = sensingUnit;
		this.trafficJunction = trafficJunction;
		this.trafficLight = trafficLight;
	}

	public SensingUnit getSensingUnit() {
		return sensingUnit;
	}

	@Override
	public TrafficJunction getOutboundTrafficJunction() {
		return this.trafficJunction;
	}

	@Override
	public TrafficJunction getInboundTrafficJunction() {
		return this.trafficJunction;
	}

	@Override
	public TrafficLight getTrafficLight() {
		return this.trafficLight;
	}
	
	public Integer getTotalDensity() {
		return totalDensity;
	}
	
	public void setIncomingDensity(Integer incomingDensity) {
		totalDensity = getSensingUnit().getResultDensity() + incomingDensity;
	}

	public static class TrafficLineBuilder {
		private SensingUnit sensingUnit;
		private TrafficJunction trafficJunction;
		
		public InboundTrafficLine inboundTrafficLine(TrafficLight trafficLight) {
			return new TrafficLine(sensingUnit, trafficJunction, trafficLight);
		}
		
		public OutboundTrafficLine outboundTrafficLine() {
			return new TrafficLine(sensingUnit, trafficJunction, null);
		}
		
		public TrafficLineBuilder withSensingUnit(SensingUnit sensingUnit) {
			this.sensingUnit = sensingUnit;
			return this;
		}
		
		public TrafficLineBuilder withTrafficJunction(TrafficJunction trafficJunction) {
			this.trafficJunction = trafficJunction;
			return this;
		}
		
	}

}


