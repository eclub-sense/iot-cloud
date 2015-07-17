package cz.esc.iot.cloudservice.sensors;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class ESCThermometer extends Sensor {
	
	protected int temperature;
	protected int pressure;
	
	public ESCThermometer() {
		super();
	}
	
	public ESCThermometer(int uuid, String secret) {
		super(uuid, SensorType.THERMOMETER, secret);
	}
	
	@Override
	public void setPayload(byte[] payload) {
		temperature = ByteBuffer.wrap(Arrays.copyOfRange(payload, 0, 4)).getInt();
		pressure = ByteBuffer.wrap(Arrays.copyOfRange(payload, 4, 8)).getInt();
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public float getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}

	@Override
	public String toString() {
		return "ESCThermometer [temperature=" + temperature + ", pressure=" + pressure + ", uuid=" + uuid + ", type="
				+ type + ", secret=" + secret + "]";
	}
}
