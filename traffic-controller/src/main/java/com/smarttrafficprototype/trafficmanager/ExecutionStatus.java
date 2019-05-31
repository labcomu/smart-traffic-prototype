package com.smarttrafficprototype.trafficmanager;

import java.util.Date;

public class ExecutionStatus {

	private int id;
	private boolean executionFailed;
	private Date start;
	private Date startAdjacentDensityCalculation;
	private Date startLocalDensityCalculation;
	private Date startTakeDecision;
	private Date startColaboration;
	private Classification classification;
	private int timeInSeconds;
	
	public ExecutionStatus(int id) {
		this.executionFailed = false;
		this.classification = Classification.COMPLETE;
		this.timeInSeconds = 0;
		this.start = new Date();
		this.
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

	public Date getStartAdjacentDensityCalculation() {
		return startAdjacentDensityCalculation;
	}

	public void setStartAdjacentDensityCalculation(Date startAdjacentDensityCalculation) {
		this.startAdjacentDensityCalculation = startAdjacentDensityCalculation;
	}

	public Date getStartLocalDensityCalculation() {
		return startLocalDensityCalculation;
	}

	public void setStartLocalDensityCalculation(Date startLocalDensityCalculation) {
		this.startLocalDensityCalculation = startLocalDensityCalculation;
	}

	public Date getStartColaboration() {
		return startColaboration;
	}

	public void setStartColaboration(Date startColaboration) {
		this.startColaboration = startColaboration;
	}
	
	public Date getStartTakeDecision() {
		return startTakeDecision;
	}

	public void setStartTakeDecision(Date startTakeDecision) {
		this.startTakeDecision = startTakeDecision;
	}
	
	public void markLocalCalculation() {
		setStartLocalDensityCalculation(new Date());
	}
	
	public void markAdjacentCalculation() {
		setStartAdjacentDensityCalculation(new Date());
	}
	
	public void markStartColaboration() {
		setStartColaboration(new Date());
	}

	public void markTakeDecision() {
		setStartTakeDecision(new Date());
	}
	
	
	
}
