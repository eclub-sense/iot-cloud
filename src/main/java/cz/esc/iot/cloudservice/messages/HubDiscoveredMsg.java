package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

/**
 * Type of message, which is send by hub when newly registered sensor
 * has been discovered.
 */
public class HubDiscoveredMsg extends HubMessage {

	@Expose private final String type = "DISCOVERED";
	@Expose private String sensor_id;
	@Expose private String sensor_uuid;
	
	public String getSensorId() {
		return sensor_id;
	}
	
	public String getSensorUuid() {
		return sensor_uuid;
	}
	
	@Override
	public String getType() {
		return "DISCOVERED";
	}
}
