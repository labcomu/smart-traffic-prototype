package com.smarttrafficprototype.trafficmanager;

import java.util.Date;

public class ExecutionCycle {

	private Integer id;
	private Date start;
	private Date end;
	private Date startAdjacent;
	private Date startLocal;
	private Date startDecision;
	private Date startCol;
	private Classification classification;
	private long duration;
	
	public ExecutionCycle(int id, Date start, Date end, Date startAdjacent, 
			Date startLocal, Date startDecision, Date startCol, long duration, 
			Classification classification) {
		setId(id);
		setStart(start);
		setEnd(end);
		setDuration(duration);
		setClassification(classification);
		setStartAdjacent(startAdjacent);
		setStartLocal(startLocal);
		setStartDecision(startDecision);
		setStartCol(startCol);
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

	public Date getStartAdjacent() {
		return startAdjacent;
	}

	public void setStartAdjacent(Date startAdjacent) {
		this.startAdjacent = startAdjacent;
	}

	public Date getStartLocal() {
		return startLocal;
	}

	public void setStartLocal(Date startLocal) {
		this.startLocal = startLocal;
	}

	public Date getStartDecision() {
		return startDecision;
	}

	public void setStartDecision(Date startDecision) {
		this.startDecision = startDecision;
	}

	public Date getStartCol() {
		return startCol;
	}

	public void setStartCol(Date startCol) {
		this.startCol = startCol;
	}
	
}
