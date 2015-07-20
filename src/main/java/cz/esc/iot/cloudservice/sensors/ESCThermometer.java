package cz.esc.iot.cloudservice.sensors;

import com.google.gson.annotations.Expose;

public class ESCThermometer extends Sensor {
	
	@Expose (deserialize = false) protected int temperature;
	@Expose (deserialize = false) protected int pressure;
	
	public ESCThermometer() {
		super();
	}
	
	public ESCThermometer(int uuid, String secret) {
		super(uuid, SensorType.THERMOMETER, secret);
	}
	
	@Override
	public void setPayload(byte[] payload) {
		temperature = payload[0];
		pressure = payload[1];
	}

	public int getTemperature() {
		return temperature;
	}

	public int getPressure() {
		return pressure;
	}

	@Override
	public String toString() {
		return "ESCThermometer [temperature=" + temperature + ", pressure=" + pressure + ", uuid=" + uuid + ", type="
				+ type + ", secret=" + secret + "]";
	}
}
