package com.smarttrafficprototype.trafficmanager.service.request;

public interface RemoteRequestService {

	<T> T requestDensity(String host, String deviceId, String[] values);
	
	default void requestSetup() {}
	
}
