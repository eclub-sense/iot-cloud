package cz.esc.iot.cloudservice.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cz.esc.iot.cloudservice.sensors.data.SensorData;

public class Sensor {

	@Expose @SerializedName("@type") private String jsonType = "sensor";
	@Expose protected int uuid;
	@Expose protected SensorType type = SensorType.THERMOMETER;
	@Expose (serialize = false) protected String secret;
	protected SensorData data;
	
	public Sensor() {
		super();
	}
	
	public Sensor(int uuid, SensorType type, String secret) {
		this.uuid = uuid;
		this.type = type;
		this.secret = secret;
	}

	public void setData(SensorData data) {
		this.data = data;
	}
	
	public SensorData getData() {
		return data;
	}
	
	public int getUuid() {
		return uuid;
	}

	public SensorType getType() {
		return type;
	}

	public String getSecret() {
		return secret;
	}

	@Override
	public String toString() {
		return "SENSOR: {\"uuid\" : " + uuid + ", \"type\" : \""+ type + "\", \"secret\" : " + secret + "}";
	}
}
