package cz.esc.iot.cloudservice.sensors;

public class ESCThermometer extends Sensor {
	
	private int temperature;
	private int pressure;
	
	public ESCThermometer() {
		super();
	}
	
	public ESCThermometer(int uuid, String secret) {
		super(uuid, SensorType.THERMOMETER, secret);
	}
	
	@Override
	public void setPayload(String payload) {
		temperature = Integer.parseInt(payload.substring(0,3));
		pressure = Integer.parseInt(payload.substring(4,7));
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
}
