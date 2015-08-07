package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

public class HubDiscoveredMsg extends HubMessage {

	@Expose private static final String type = "DISCOVERED";
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
		return type;
	}
}
