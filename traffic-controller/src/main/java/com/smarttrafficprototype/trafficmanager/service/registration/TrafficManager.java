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

import com.smarttrafficprototype.trafficmanager.CSVWriter;
import com.smarttrafficprototype.trafficmanager.Classification;
import com.smarttrafficprototype.trafficmanager.ExecutionCyclesRepository;
import com.smarttrafficprototype.trafficmanager.ExecutionStatus;
import com.smarttrafficprototype.trafficmanager.Setup;
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
	@Autowired
	private Setup setup;
	
	@Value("${setup.greenLight.initialDurationInSec}")
	private Integer greenLightDuration = 10;
	@Value("${setup.greenLight.minimumDuration}")
	private Integer minimumGreenLightDuration = 10;
	@Value("${setup.greenLight.maximumDuration}")
	private Integer maximumGreenLightDuration = 60;
	@Value("${setup.trafficJunctionCycleDuration}")
	private Integer trafficJunctionCycleDuration = 180;
	@Value("${setup.windowTimeCalculationInSec}")
	private Integer windowTimeCalculation = 5;
	@Value("${setup.executionCycleDurationInMili}")
	private Long executionCycleDuration = 1000l;
	@Value("${setup.experimentDurationInMili}")
	private Long experimentDuration = 120_000l;
	@Autowired
	private CSVWriter csvWriter;
	
	private Date greenLightStartTime;
	private Integer greenLightTimeRemaining;
	private InboundTrafficLine lineMaxDensity;
	
	private static int count = 0;

	
	@Scheduled(initialDelayString="${setup.initialExecutionDelayInMili}", fixedRateString="${setup.executionCycleDurationInMili}")
	public void run() throws Exception {
		
		if (!setup.isActive()) {
			logger.info("Execution is disabled.");
			return;
		}
		
		if (isExperimentOver()) {
			csvWriter.writeCSV();
			count = 0;
			logger.info("Execution is over.");
			return;
		}
		count++;
		ExecutionStatus execution = setupExecution();
		
		logger.info("Starting execution #ID" + execution.getId());
		
		triggerSensors();
		
		Long greenLightTimeElapsed = new Date().getTime() - greenLightStartTime.getTime();
		greenLightTimeRemaining =  greenLightDuration - (greenLightTimeElapsed.intValue() / MILLISECONDS);
		
		logger.info("#ID" + execution.getId() + ": Green light remainging time: " + greenLightTimeRemaining + " seconds");
		
		//trafficJunction.toString();
		
		calculateNextTimeGreenLight(execution);
		
		if (execution.isExecutionFailed()) {
			return;
		}
		
		changeTrafficLineGreenLight(execution);
		
		logExecution(execution);
	}

	private ExecutionStatus setupExecution() {
		return new ExecutionStatus(count);
	}

	private boolean isExperimentOver() {
		return count >= (experimentDuration/executionCycleDuration);
	}

	private void logExecution(ExecutionStatus execution) {
		repository.addExecution(getCurrentDuration(execution), execution);
	}

	private void changeTrafficLineGreenLight(ExecutionStatus execution) {
		if (greenLightTimeRemaining < FINAL_SECOND) {
			setGreenLightDuration(execution);
			
			InboundTrafficLine greenTrafficLine = trafficJunction.getInboundLines()
					.stream().filter((inLn) -> inLn.getTrafficLight().isGreen()).findFirst().get();
			
			lineMaxDensity.getTrafficLight().turnGreen();
			greenLightStartTime = new Date();
			
			greenTrafficLine.getSensingUnit().clearDensity();
			greenTrafficLine.getTrafficLight().turnRed();
			
			trafficJunction.getOutboundLines().forEach((out) -> out.getSensingUnit().clearDensity());
			
		}
	}

	private void setGreenLightDuration(ExecutionStatus execution) {
		if (execution.getTimeInSeconds() <= minimumGreenLightDuration) {
			greenLightDuration = minimumGreenLightDuration;
		} else if (execution.getTimeInSeconds() > maximumGreenLightDuration) {
			greenLightDuration = maximumGreenLightDuration;
		} else {
			greenLightDuration = execution.getTimeInSeconds();
		}
	}

	private void calculateNextTimeGreenLight(ExecutionStatus execution) {
		//if (greenLightTimeRemaining > FINAL_SECOND) {
			logger.info("#ID" + execution.getId() + " start of next green light calculation.");
			List<InboundTrafficLine> redLines = trafficJunction.getInboundLines()
					.stream().filter((inLn) -> inLn.getTrafficLight().isRed()).collect(Collectors.toList());
			
			getIncommingDensityFromNeighborJunctions(redLines, execution);
			
			if (execution.isExecutionFailed()) {
				return;
			}
			
			lineMaxDensity = getMaximumTrafficLine(redLines);
			
			int totalDensity = redLines.stream().map((line) -> line.getSensingUnit().getResultDensity()).reduce(Integer::sum).get();
			
			if (totalDensity > 0) {
				Double calcTime = (lineMaxDensity.getTotalDensity().doubleValue() / totalDensity) * trafficJunctionCycleDuration;
				execution.setTimeInSeconds(calcTime.intValue());
			} else {
				execution.setTimeInSeconds(0);
			}
			
			logger.info("#ID" + execution.getId() + "Calculating time of next green: (" + lineMaxDensity.getTotalDensity() + "/" + totalDensity + ") * "+trafficJunctionCycleDuration+" = " + execution.getTimeInSeconds() );
			
		//}
	}
	
	private void getIncommingDensityFromNeighborJunctions(List<InboundTrafficLine> inboundLines, ExecutionStatus execution) {
		for (InboundTrafficLine trafficLine : inboundLines) {
			TrafficJunction inboundTrafficJunction = trafficLine.getInboundTrafficJunction();
			Integer incomingDensity = 0;
			try {
				if (inboundTrafficJunction != null) {
					incomingDensity = requestService.requestDensity(
							inboundTrafficJunction.getHost(), inboundTrafficJunction.getPort(), new String[]{trafficJunction.getJunctionKey()});
					
					logger.info("#ID" + execution.getId() + "I ncoming density: " + incomingDensity + " from " + trafficLine.getInboundTrafficJunction().getJunctionKey());
				}
			} catch (Exception ex) {
				incomingDensity = 0;
				if (getCurrentDuration(execution) > executionCycleDuration) {
					execution.setClassification(Classification.FAILED);
					execution.setExecutionFailed(true);
					logger.info("#ID" + execution.getId() + " execution failed");
					logExecution(execution);
					return;
				} else {
					logger.info("#ID" + execution.getId() + " execution incomplete");
					execution.setClassification(Classification.INCOMPLETE);
				}
			}
			
			trafficLine.setIncomingDensity(incomingDensity);
		}
	}
	
	private long getCurrentDuration(ExecutionStatus execution) {
		return new Date().getTime() - execution.getStarting();
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
	
	public void startup() {
		greenLightStartTime = new Date();
	}

}
