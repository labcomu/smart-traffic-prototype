package com.smarttrafficprototype.trafficmanager;

import java.util.Date;

public class ExecutionStatus {

	private int id;
	private boolean executionFailed;
	private Date start;
	private Classification classification;
	private int timeInSeconds;
	
	public ExecutionStatus(int id) {
		this.executionFailed = false;
		this.classification = Classification.COMPLETE;
		this.timeInSeconds = 0;
		this.start = new Date();
		setId(id);
	}

	public boolean isExecutionFailed() {
		return executionFailed;
	}

	public void setExecutionFailed(boolean executionFailed) {
		this.executionFailed = executionFailed;
	}

	public long getStarting() {
		return getStart().getTime();
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
	
	
	
}
