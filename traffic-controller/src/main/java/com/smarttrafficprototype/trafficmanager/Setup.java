package com.smarttrafficprototype.trafficmanager;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Setup {
	
	private boolean active = false;
	
	public void activate() {
		this.active = true;
	}
	
	public void deactivate() {
		this.active = true;
	}

	public boolean isActive() {
		return active;
	}
}
