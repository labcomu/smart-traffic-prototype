package com.smarttrafficprototype.trafficmanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ExecutionCyclesRepository {
	
	public Logger logger = LoggerFactory.getLogger(getClass());
	
	private List<ExecutionCycle> cycles = new ArrayList<>();
	private static int count;
	
	public List<ExecutionCycle> getAll() {
		return cycles;
	}
	
	public void addExecution(long duration, Classification classification) {
		count++;
		Date moment = new Date();
		logger.info("#ID: " + count + "; Moment:" + moment + "; Duration: " + duration + "; Classification: " + classification);
		cycles.add(new ExecutionCycle(count, new Date(), duration, classification));
	}
	
}
