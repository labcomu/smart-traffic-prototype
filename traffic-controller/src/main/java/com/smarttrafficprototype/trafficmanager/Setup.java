package com.smarttrafficprototype.trafficmanager;

import java.util.Date;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Setup {
	
	private boolean active = false;
	private long startTime;
	
	public void activate() {
		this.active = true;
	}
	
	public void deactivate() {
		this.active = true;
	}

	public boolean isActive() {
		return active;
	}
	
	public long getExecutionTime() {
		long currentTime = (new Date()).getTime();
		
		return currentTime - startTime;
		
	}
}
