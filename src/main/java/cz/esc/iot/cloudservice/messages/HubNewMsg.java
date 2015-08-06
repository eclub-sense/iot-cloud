package cz.esc.iot.cloudservice.messages;

import java.util.Collection;

import com.google.gson.annotations.Expose;

import cz.esc.iot.cloudservice.persistance.model.SensorEntity;

public class HubNewMsg extends HubMessage {
	
	@Expose private static final String type = "NEW";
	@Expose private Collection<SensorEntity> sensors;
	
	public HubNewMsg(Collection<SensorEntity> sensors) {
		this.sensors = sensors;
	}

	@Override
	public String getType() {
		return type;
	}
}
