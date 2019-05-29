package com.smarttrafficprototype.trafficmanager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FailureManager {

	@Value("${failure.file.path}")
	private String failureFilePath;

	@Autowired
	private Setup setup;

	private List<FailureOcurrence> failures = new ArrayList<FailureOcurrence>();
	
	public Logger logger = LoggerFactory.getLogger(getClass());
	

	@Value("${failure.enabled:false}")
	private boolean failureEnabled;


	@PostConstruct
	private void start() {
		
		if(failureEnabled) {
			loadFailureFile();
		}
	}
	
	
	
	private void loadFailureFile() {
		try {
			logger.info("Starting the load of failures definition from file");

			List<String> lines = Files.readAllLines(Paths.get(failureFilePath));
			for (String line : lines) {
				String[] lineValues = line.split(";");
				for (String value : lineValues) {
					String[] split = value.split(":");
					Integer failureMoment = Integer.valueOf(split[0]);
					Integer failureDuration = Integer.valueOf(split[1]);
					failures.add(new FailureOcurrence(failureMoment*1000l, failureDuration*1000l));
				}
				
			logger.info("Failures definition loaded");


			}
		} catch (IOException e) {
			logger.info("Error trying to load failures definition file ("+failureFilePath+"). Considering no device failures!");
		}
	}

	public boolean isFailed() {
		long executionTime = setup.getExecutionTime();
		boolean isFailed = false;

		if (setup.isActive()) {

			for (FailureOcurrence failure : failures) {

				if (executionTime >= failure.getStart() && executionTime <= failure.getEnd()) {
					isFailed = true;
					break;
				}
			}

		}
		
		return isFailed;

	}

}