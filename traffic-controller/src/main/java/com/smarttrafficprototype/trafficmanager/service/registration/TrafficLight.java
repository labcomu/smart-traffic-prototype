package com.smarttrafficprototype.trafficmanager.service.registration;

public class TrafficLight {
	
	private TrafficLightSignal signal;
	private InboundTrafficLine inboundTrafficLine;
	
	public TrafficLight(TrafficLightSignal signal) {
		this.signal = signal;
	}
	
	public TrafficLight() {
		this.signal = TrafficLightSignal.RED;
	}

	public TrafficLightSignal getSignal() {
		return signal;
	}

	public void setSignal(TrafficLightSignal signal) {
		this.signal = signal;
	}

	public InboundTrafficLine getInboundTrafficLine() {
		return inboundTrafficLine;
	}

	public void setInboundTrafficLine(InboundTrafficLine inboundTrafficLine) {
		this.inboundTrafficLine = inboundTrafficLine;
	}
	
	public void turnGreen() {
		this.signal = TrafficLightSignal.GREEN;
	}
	
	public void turnRed() {
		this.signal = TrafficLightSignal.RED;
	}
	
	public void turnOrange() {
		this.signal = TrafficLightSignal.ORANGE;
	}
	
	public boolean isRed() {
		return this.signal.equals(TrafficLightSignal.RED);
	}
	
	public boolean isGreen() {
		return this.signal.equals(TrafficLightSignal.GREEN);
	}

	public boolean isOrange() {
		return this.signal.equals(TrafficLightSignal.ORANGE);
	}
}
