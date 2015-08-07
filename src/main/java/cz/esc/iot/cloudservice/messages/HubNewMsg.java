package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

import cz.esc.iot.cloudservice.persistance.model.SensorEntity;

public class HubNewMsg extends HubMessage {
	
	@Expose private static final String type = "NEW";
	@Expose private SensorEntity sensor;
	
	public HubNewMsg(SensorEntity sensor) {
		this.sensor = sensor;
	}

	@Override
	public String getType() {
		return type;
	}
}
