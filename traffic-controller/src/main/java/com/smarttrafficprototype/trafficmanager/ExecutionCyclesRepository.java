package com.smarttrafficprototype.trafficmanager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class ExecutionCyclesRepository {
	
	private List<ExecutionCycle> cycles = new ArrayList<>();
	private static int count;
	
	public List<ExecutionCycle> getAll() {
		return cycles;
	}
	
	public void addExecution(long duration, Classification classification) {
		cycles.add(new ExecutionCycle(++count, new Date(), duration, classification));
	}
	
}
