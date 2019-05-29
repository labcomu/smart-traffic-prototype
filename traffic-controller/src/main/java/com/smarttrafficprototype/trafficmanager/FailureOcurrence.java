package com.smarttrafficprototype.trafficmanager;

public class FailureOcurrence {

	private Long start;
	private Long duration;
	
	public FailureOcurrence(Long start, Long duration) {
		this.start = start;
		this.duration = duration;
	}
	
	public Long getStart() {
		return start;
	}
	
	public Long getEnd() {
		
		return start+duration;
	}
	public Long getDuration() {
		return duration;
	}
	
}
