package com.smarttrafficprototype.trafficmanager.service.registration;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.smarttrafficprototype.trafficmanager.service.ServiceRegistry;
import com.smarttrafficprototype.trafficmanager.service.request.RemoteRequestService;

@Component
public class TrafficManager {
	private static final int MILLISECONDS = 1000;

	public Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TrafficJunction trafficJunction;
	@Autowired
	private RemoteRequestService requestService;
	@Autowired
	private ServiceRegistry serviceRegistry;
	
	@Value("${setup.greenSignDurationInSec}")
	private Integer greenSignDuration = 10;
	@Value("${setup.windowTimeCalculationInSec}")
	private Integer windowTimeCalculation = 5;
	@Value("${setup.minimumGreenLightDuration}")
	private Integer minimumGreenLightDuration = 10;
	@Value("${setup.maximumGreenLightDuration}")
	private Integer maximumGreenLightDuration = 60;
	@Value("${setup.maximumGreenLightDuration}")
	private Integer trafficJunctionCycleDuration = 180;
	
	private Date greenLightStartTime = new Date();
	private boolean nextLineChosen;
	private static int timeInSeconds;
	private InboundTrafficLine lineMaxDensity;
	
	
	
	@Scheduled(initialDelay=0, fixedDelayString="${setup.calculationTimeDurationInMili}")
	public void run() throws Exception {
		triggerSensors();
		
		Long greenLightTimeElapsed = new Date().getTime() - greenLightStartTime.getTime();
		Integer greenLightTimeRemaining =  greenSignDuration - (greenLightTimeElapsed.intValue() / MILLISECONDS);
		
		logger.info("Green light remainging time: " + greenLightTimeRemaining + " seconds");
		
		trafficJunction.toString();
		
		if (greenLightTimeRemaining > windowTimeCalculation) {
			return;
		}
		
		
		calculateNextTimeGreenSign();
		
		changeTrafficLineGreenSign(greenLightTimeRemaining);
	}

	private void changeTrafficLineGreenSign(Integer greenLightTimeRemaining) {
		if (greenLightTimeRemaining < 1) {
			setGreenLightDuration();
			
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

	private void setGreenLightDuration() {
		if (timeInSeconds <= minimumGreenLightDuration) {
			greenSignDuration = minimumGreenLightDuration;
		} else if (timeInSeconds > maximumGreenLightDuration) {
			greenSignDuration = maximumGreenLightDuration;
		} else {
			greenSignDuration = timeInSeconds;
		}
	}

	private void calculateNextTimeGreenSign() {
		if (!nextLineChosen) {
			List<InboundTrafficLine> redLines = trafficJunction.getInboundLines()
					.stream().filter((inLn) -> inLn.getTrafficLight().isRed()).collect(Collectors.toList());
			
			getIncommingDensityFromNeighborJunctions(redLines);
			lineMaxDensity = getMaximumTrafficLine(redLines);
			
			int totalDensity = redLines.stream().map((line) -> line.getSensingUnit().getResultDensity()).reduce(Integer::sum).get();
			
			if (totalDensity > 0) {
				Double calcTime = (lineMaxDensity.getTotalDensity().doubleValue() / totalDensity) * trafficJunctionCycleDuration;
				timeInSeconds = calcTime.intValue();
			} else {
				timeInSeconds = 0;
			}
			
			logger.info("Calculating time of next green: (" + lineMaxDensity.getTotalDensity() + "/" + totalDensity + ") * "+trafficJunctionCycleDuration+" = " + timeInSeconds );
			
			nextLineChosen = true;
		}
	}
	
	private void getIncommingDensityFromNeighborJunctions(List<InboundTrafficLine> inboundLines) {
		for (InboundTrafficLine trafficLine : inboundLines) {
			TrafficJunction inboundTrafficJunction = trafficLine.getInboundTrafficJunction();
			Integer incomingDensity = 0;
			if (inboundTrafficJunction != null) {
				try {
					incomingDensity = requestService.requestDensity(
							inboundTrafficJunction.getHost(), inboundTrafficJunction.getPort(), new String[]{trafficJunction.getJunctionKey()});
					
					logger.info("Incoming density: " + incomingDensity + " from " + trafficLine.getInboundTrafficJunction().getJunctionKey());
				} catch (Exception ex) {
					incomingDensity = 0;
				}
			}
			
			trafficLine.setIncomingDensity(incomingDensity);
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
			if (lineMaxDensity == null || trafficLine.getTotalDensity() > lineMaxDensity.getTotalDensity()) {
				lineMaxDensity = trafficLine;
			}
			
		}
		return lineMaxDensity;
	}

}
