package com.smarttrafficprototype.trafficmanager;

import java.util.Date;

public class ExecutionStatus {

	private boolean executionFailed;
	private long starting;
	private Classification classification;
	private int timeInSeconds;
	
	public ExecutionStatus() {
		this.executionFailed = false;
		this.starting = new Date().getTime();
		this.classification = Classification.COMPLETE;
		this.timeInSeconds = 0;
	}

	public boolean isExecutionFailed() {
		return executionFailed;
	}

	public void setExecutionFailed(boolean executionFailed) {
		this.executionFailed = executionFailed;
	}

	public long getStarting() {
		return starting;
	}

	public void setStarting(long starting) {
		this.starting = starting;
	}

	public Classification getClassification() {
		return classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	public int getTimeInSeconds() {
		return timeInSeconds;
	}

	public void setTimeInSeconds(int timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}
	
	
	
	
}
