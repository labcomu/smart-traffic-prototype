package com.smarttrafficprototype.trafficmanager;

import java.util.Date;

public class ExecutionCycle {

	private Integer id;
	private Date start;
	private Date end;
	private Classification classification;
	private long duration;
	
	public ExecutionCycle(int id, Date start, Date end, long duration, Classification classification) {
		setId(id);
		setEnd(end);
		setDuration(duration);
		setClassification(classification);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date moment) {
		this.end = moment;
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

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
}
