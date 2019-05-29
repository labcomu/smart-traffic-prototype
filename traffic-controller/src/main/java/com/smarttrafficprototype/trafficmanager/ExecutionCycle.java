package com.smarttrafficprototype.trafficmanager;

import java.util.Date;

public class ExecutionCycle {

	private Integer id;
	private Date moment;
	private Classification classification;
	private long duration;
	
	public ExecutionCycle(int id, Date date, long duration, Classification classification) {
		setId(id);
		setMoment(date);
		setDuration(duration);
		setClassification(classification);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getMoment() {
		return moment;
	}
	public void setMoment(Date moment) {
		this.moment = moment;
	}
	public Classification getClassification() {
		return classification;
	}
	public void setClassification(Classification classification) {
		this.classification = classification;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	
}
