package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

/**
 * Class for type of message which is send from zettor to hub when
 * new sensor has been registered by user.
 */
public class HubNewMsg extends HubMessage {
	
	@Expose private final String type = "NEW";
	@Expose private int sensor_type;
	
	public HubNewMsg(String sensor, int t) {
		this.uuid = sensor;
		sensor_type = t;
	}

	@Override
	public String getType() {
		return "NEW";
	}
	
	@Override
	public String toString() {
		return "HubNewMsg [type=" + type + ", uuid=" + uuid + "]";
	}
}
