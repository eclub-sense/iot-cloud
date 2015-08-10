package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

public class HubNewMsg extends HubMessage {
	
	@Expose private final String type = "NEW";
	@Expose private int sensor_type;
	
	public HubNewMsg(String sensor, int t) {
		this.uuid = sensor;
		sensor_type = t;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		return "HubNewMsg [type=" + type + ", uuid=" + uuid + "]";
	}
}
