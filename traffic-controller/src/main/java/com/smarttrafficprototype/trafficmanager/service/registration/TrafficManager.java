package com.smarttrafficprototype.trafficmanager.service.registration;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smarttrafficprototype.trafficmanager.service.ServiceRegistry;
import com.smarttrafficprototype.trafficmanager.service.request.RemoteRequestService;

@Component
public class TrafficManager {
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TrafficJunction trafficJunction;
	@Autowired
	private RemoteRequestService requestService;
	@Autowired
	private ServiceRegistry serviceRegistry;
	private Integer greenSignDuration = 30;
	private Date greenLightStartTime = new Date();
	private Integer windowTimeCalculation = 5;
	
	private boolean nextLineChosen;

	private int timeInSeconds;

	private InboundTrafficLine lineMaxDensity;
	
	@Scheduled(initialDelay=0, fixedDelay=1000)
	public void run() throws Exception {
		triggerSensors();
		
		Long greenLightTimeElapsed = new Date().getTime() - greenLightStartTime.getTime();
		Integer greenLightTimeRemaining =  greenSignDuration - (greenLightTimeElapsed.intValue() / 1000);
		
		logger.info("Green light remainging time: " + greenLightTimeRemaining + " seconds");
		
		trafficJunction.toString();
		if (greenLightTimeRemaining > windowTimeCalculation) {
			return;
		}
		
		if (!nextLineChosen) {
			//busca a via de maior densidade
			List<InboundTrafficLine> redLines = trafficJunction.getInboundLines()
					.stream().filter((inLn) -> inLn.getTrafficLight().isRed()).collect(Collectors.toList());
			
			lineMaxDensity = getMaximumTrafficLine(redLines);
			
			//calcula o tempo de sinal verde da via
			Integer totalDensity = redLines.stream().map((line) -> line.getSensingUnit().getResultDensity()).reduce(Integer::sum).get();
			
			timeInSeconds = totalDensity == 0 ? totalDensity : (lineMaxDensity.getTotalDensity() / totalDensity) * 180;
			
			logger.info("Calculating time of next green: (" + lineMaxDensity.getTotalDensity() + "/" + totalDensity + ") * 180 = " + timeInSeconds );
			
			nextLineChosen = true;
		} 
		
		if (greenLightTimeRemaining < 1) {
			if (timeInSeconds < 10) {
				greenSignDuration = 10;
			} else if (timeInSeconds > 60) {
				greenSignDuration = 60;
			} else {
				greenSignDuration = timeInSeconds;
			}
			
			InboundTrafficLine greenTrafficLine = trafficJunction.getInboundLines()
					.stream().filter((inLn) -> inLn.getTrafficLight().isGreen()).findFirst().get();
			
			lineMaxDensity.getTrafficLight().turnGreen();
			greenLightStartTime = new Date();
			
			greenTrafficLine.getSensingUnit().clearDensity();
			greenTrafficLine.getTrafficLight().turnRed();
			
			trafficJunction.getOutboundLines().forEach((out) -> out.getSensingUnit().clearDensity());
			
			nextLineChosen = false;
		}
	}
	
	private void triggerSensors() {
		for (Sensor sensor : serviceRegistry.getSensors()) {
			sensor.notifySensingUnit();
		}
	}

	private InboundTrafficLine getMaximumTrafficLine(List<InboundTrafficLine> inboundLines) {
		InboundTrafficLine lineMaxDensity = null;
		for (InboundTrafficLine trafficLine : inboundLines) {
			
			TrafficJunction inboundTrafficJunction = trafficLine.getInboundTrafficJunction();
			Integer incomingDensity = 0;
			if (inboundTrafficJunction != null) {
				incomingDensity = requestService.requestDensity(
						inboundTrafficJunction.getHost(), inboundTrafficJunction.getPort(), new String[]{trafficJunction.getJunctionKey()});
				
				logger.info("Incoming density: " + incomingDensity + " from " + trafficLine.getInboundTrafficJunction().getJunctionKey());
			}
			
			trafficLine.setIncomingDensity(incomingDensity);
			
			//incrementar densidade com o valor obtido da OutboundTrafficLine
			if (lineMaxDensity == null || trafficLine.getTotalDensity() > lineMaxDensity.getTotalDensity()) {
				lineMaxDensity = trafficLine;
			}
			
		}
		return lineMaxDensity;
	}

}
