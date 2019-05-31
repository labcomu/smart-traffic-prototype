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
	
	public List<ExecutionCycle> getAll() {
		cycles.sort((e1, e2) -> e1.getId()-e2.getId());
		return cycles;
	}
	
	public void addExecution(long duration, ExecutionStatus execution) {
		Date moment = new Date();
		logger.info("#ID: " + execution.getId() + "; Moment:" + moment + "; Duration: " + duration + "; Classification: " + execution.getClassification());
		cycles.add(new ExecutionCycle(execution.getId(), new Date(), duration, execution.getClassification()));
	}
	
}
