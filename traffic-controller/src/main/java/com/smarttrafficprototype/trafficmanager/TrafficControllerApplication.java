package com.smarttrafficprototype.trafficmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages= {"com.smarttrafficprototype", "com.microthingsexperiment"} )
@EnableScheduling
public class TrafficControllerApplication  {

	public static void main(String[] args) {
		SpringApplication.run(TrafficControllerApplication.class, args);
	}
	
}
