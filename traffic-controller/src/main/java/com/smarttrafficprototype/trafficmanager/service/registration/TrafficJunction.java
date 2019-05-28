package com.smarttrafficprototype.trafficmanager.service.registration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.smarttrafficprototype.trafficmanager.service.SensingUnitImpl;
import com.smarttrafficprototype.trafficmanager.service.ServiceRegistry;

@Component
public class TrafficJunction extends ServiceComponent {
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ServiceRegistry serviceRegistry;

	private String junctionKey;
	private List<InboundTrafficLine> inboundLines = new ArrayList<>();
	private List<OutboundTrafficLine> outboundLines = new ArrayList<>();
	
	@PostConstruct
	public void setup() {
		buildInboundLines();
		buildOutboundLines();
		setJunctionKey(serviceRegistry.getCurrentJunction().getJunctionKey());
	}
	
	public List<InboundTrafficLine> getInboundLines() {
		return Collections.unmodifiableList(this.inboundLines);
	}
	
	public List<OutboundTrafficLine> getOutboundLines() {
		return Collections.unmodifiableList(this.outboundLines);
	}

	public String getJunctionKey() {
		return junctionKey;
	}

	public void setJunctionKey(String junctionKey) {
		this.junctionKey = junctionKey;
	}
	
	private void buildInboundLines() {
		for (ServiceRegistry.InboundLine inboundLine: serviceRegistry.getInboundLines()) {
			
			List<Sensor> sensorArray = new ArrayList<>();
			Sensor sensor = new IRSensor();
			sensorArray.add(sensor);
			SensingUnitImpl unit = new SensingUnitImpl(sensorArray);
			sensor.registerSensingUnit(unit);
			serviceRegistry.registrySensor(sensor);
			
			TrafficLine.TrafficLineBuilder builder = new TrafficLine.TrafficLineBuilder()
																.withSensingUnit(unit);
			TrafficJunction tj = null;
			if (!inboundLine.getOriginJunction().equals("NONE")) {
				tj =  serviceRegistry.getTrafficJunctions().get(
						Integer.parseInt(inboundLine.getOriginJunction())
					);
			}
			
			builder.withTrafficJunction(tj);
			TrafficLight light = new TrafficLight(
					TrafficLightSignal.valueOf(inboundLine.getStartingLight())
				);
			
			inboundLines.add(builder.inboundTrafficLine(light));
		}
	}
	
	private void buildOutboundLines() {
		for (ServiceRegistry.OutboundLine outboundLine : serviceRegistry.getOutboundLines()) {
			
			List<Sensor> sensorArray = new ArrayList<>();
			Sensor sensor = new IRSensor();
			sensorArray.add(sensor);
			SensingUnitImpl unit = new SensingUnitImpl(sensorArray);
			sensor.registerSensingUnit(unit);
			serviceRegistry.registrySensor(sensor);
			
			TrafficLine.TrafficLineBuilder builder = new TrafficLine.TrafficLineBuilder()
																.withSensingUnit(unit);
			TrafficJunction tj = null;
			if (!outboundLine.getDestinationJunction().equals("NONE")) {
				tj = serviceRegistry.getTrafficJunctions().get(
						Integer.parseInt(outboundLine.getDestinationJunction())
					);
			}
			builder.withTrafficJunction(tj);
			outboundLines.add(builder.outboundTrafficLine());
		}
	}
	
	@Override
	public String toString() {
		StringBuilder tjStr = new StringBuilder();
		tjStr.append("IBounds: ");
		int i = 0;
		for (InboundTrafficLine inboundTrafficLine : inboundLines) {
			tjStr.append("TL")
				.append(i)
				.append(" ( D: ")
				.append(inboundTrafficLine.getSensingUnit().getResultDensity())
				.append(", S: ")
				.append(inboundTrafficLine.getTrafficLight().getSignal().toString())
				.append(" ); ");
			i++;
		}
		i = 0;
		tjStr.append("OBounds: ");
		for (OutboundTrafficLine outboundTrafficLine : outboundLines) {
			tjStr.append("TL")
				.append(i)
				.append(" ( D: ")
				.append(outboundTrafficLine.getSensingUnit().getResultDensity())
				.append("); ");
			i++;
		}
		logger.info(tjStr.toString());
		return super.toString();
	}
	
}
