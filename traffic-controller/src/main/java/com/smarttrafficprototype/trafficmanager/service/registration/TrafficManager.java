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

import com.smarttrafficprototype.trafficmanager.Classification;
import com.smarttrafficprototype.trafficmanager.ExecutionCyclesRepository;
import com.smarttrafficprototype.trafficmanager.service.ServiceRegistry;
import com.smarttrafficprototype.trafficmanager.service.request.RemoteRequestService;

@Component
public class TrafficManager {
	private static final int FINAL_SECOND = 1;

	private static final int MILLISECONDS = 1000;

	public Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private TrafficJunction trafficJunction;
	@Autowired
	private RemoteRequestService requestService;
	@Autowired
	private ServiceRegistry serviceRegistry;
	@Autowired
	private ExecutionCyclesRepository repository;
	
	@Value("${setup.greenLight.initialDurationInSec}")
	private Integer initialGreenLightDuration = 10;
	@Value("${setup.greenLight.minimumDuration}")
	private Integer minimumGreenLightDuration = 10;
	@Value("${setup.greenLight.maximumDuration}")
	private Integer maximumGreenLightDuration = 60;
	@Value("${setup.trafficJunctionCycleDuration}")
	private Integer trafficJunctionCycleDuration = 180;
	@Value("${setup.windowTimeCalculationInSec}")
	private Integer windowTimeCalculation = 5;
	@Value("${setup.setup.executionCycleDurationInMili}")
	private Long executionCycleDuration = 1000l;
	
	private Date greenLightStartTime = new Date();
	private boolean nextLineChosen;
	private InboundTrafficLine lineMaxDensity;
	
	private static long starting;
	private static Classification classification;
	private static int timeInSeconds;
	
	
	
	
	@Scheduled(initialDelay=0, fixedDelayString="${setup.setup.executionCycleDurationInMili}")
	public void run() throws Exception {
		starting = new Date().getTime();
		classification = Classification.COMPLETE;
		
		triggerSensors();
		
		Long greenLightTimeElapsed = new Date().getTime() - greenLightStartTime.getTime();
		Integer greenLightTimeRemaining =  initialGreenLightDuration - (greenLightTimeElapsed.intValue() / MILLISECONDS);
		
		logger.info("Green light remainging time: " + greenLightTimeRemaining + " seconds");
		
		trafficJunction.toString();
		
		if (greenLightTimeRemaining > windowTimeCalculation) {
			return;
		}
		
		
		calculateNextTimeGreenLight();
		
		changeTrafficLineGreenLight(greenLightTimeRemaining);
		
		logExecution();
	}

	private void logExecution() {
		repository.addExecution(getCurrentDuration(), classification);
	}

	private void changeTrafficLineGreenLight(Integer greenLightTimeRemaining) {
		if (greenLightTimeRemaining < FINAL_SECOND) {
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
			initialGreenLightDuration = minimumGreenLightDuration;
		} else if (timeInSeconds > maximumGreenLightDuration) {
			initialGreenLightDuration = maximumGreenLightDuration;
		} else {
			initialGreenLightDuration = timeInSeconds;
		}
	}

	private void calculateNextTimeGreenLight() {
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
			try {
				if (inboundTrafficJunction != null) {
					incomingDensity = requestService.requestDensity(
							inboundTrafficJunction.getHost(), inboundTrafficJunction.getPort(), new String[]{trafficJunction.getJunctionKey()});
					
					logger.info("Incoming density: " + incomingDensity + " from " + trafficLine.getInboundTrafficJunction().getJunctionKey());
				}
			} catch (Exception ex) {
				incomingDensity = 0;
				if (getCurrentDuration() > executionCycleDuration) {
					classification = Classification.INCOMPLETE;
				} else {
					classification = Classification.PARTIALlY_COMPLETE;
				}
			}
			
			trafficLine.setIncomingDensity(incomingDensity);
		}
	}
	
	private long getCurrentDuration() {
		return new Date().getTime() - starting;
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
