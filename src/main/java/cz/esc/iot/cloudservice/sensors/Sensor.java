package cz.esc.iot.cloudservice.sensors;

import cz.esc.iot.cloudservice.sensors.data.SensorData;

public class Sensor {

	private int uuid;
	private SensorType type;
	private int secret;
	private SensorData data;
	
	public Sensor(int uuid, SensorType type, int secret) {
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

	public int getSecret() {
		return secret;
	}

	@Override
	public String toString() {
		return "{\"uuid\" : " + uuid + ", \"type\" : \""+ type + "\", \"secret\" : " + secret + "}";
	}
}
