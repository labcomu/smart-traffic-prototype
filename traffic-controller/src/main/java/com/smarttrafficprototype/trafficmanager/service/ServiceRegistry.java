package com.smarttrafficprototype.trafficmanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.smarttrafficprototype.trafficmanager.service.registration.Gateway;
import com.smarttrafficprototype.trafficmanager.service.registration.Sensor;
import com.smarttrafficprototype.trafficmanager.service.registration.TrafficJunction;

@Configuration
@ConfigurationProperties(prefix="setup")
public class ServiceRegistry {
	
	private Gateway gateway;
	
	private List<TrafficJunction> junctions = new ArrayList<>();
	private List<Sensor> sensors = new ArrayList<>();
	private List<InboundLine> inboundLines = new ArrayList<>();
	private List<OutboundLine> outboundLines = new ArrayList<>();
	
	private Integer idCurrentJunction;
	
	
	public List<TrafficJunction> getTrafficJunctions() {
		return getJunctions();
	}
	
	public void setTrafficJunctions(List<TrafficJunction> junctions) {
		this.setJunctions(junctions);
	}

	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}
	
	public void registrySensor(Sensor sensor) {
		sensors.add(sensor);
	}

	public List<TrafficJunction> getJunctions() {
		return junctions;
	}

	public void setJunctions(List<TrafficJunction> junctions) {
		this.junctions = junctions;
	}
	
	public List<InboundLine> getInboundLines() {
		return inboundLines;
	}

	public void setInboundLines(List<InboundLine> inboundLines) {
		this.inboundLines = inboundLines;
	}

	public List<OutboundLine> getOutboundLines() {
		return outboundLines;
	}

	public void setOutboundLines(List<OutboundLine> outboundLines) {
		this.outboundLines = outboundLines;
	}

	public TrafficJunction getCurrentJunction() {
		return junctions.get(getIdCurrentJunction());
	}


	public Integer getIdCurrentJunction() {
		return idCurrentJunction;
	}

	public void setIdCurrentJunction(Integer idCurrentJunction) {
		this.idCurrentJunction = idCurrentJunction;
	}


	public static class InboundLine {
		private String originJunction;
		private String startingLight;
		public String getOriginJunction() {
			return originJunction;
		}
		public void setOriginJunction(String originJunciton) {
			this.originJunction = originJunciton;
		}
		public String getStartingLight() {
			return startingLight;
		}
		public void setStartingLight(String startingLight) {
			this.startingLight = startingLight;
		}
		
	}
	
	public static class OutboundLine {
		private String destinationJunction;

		public String getDestinationJunction() {
			return destinationJunction;
		}

		public void setDestinationJunction(String destinationJunction) {
			this.destinationJunction = destinationJunction;
		}
		
	}

}
