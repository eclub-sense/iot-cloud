package cz.esc.iot.cloudservice.sensors;

public class ESCThermometer extends Sensor {
	
	private float temperature;
	private float pressure;
	
	public ESCThermometer() {
		super();
	}
	
	public ESCThermometer(int uuid, String secret) {
		super(uuid, SensorType.THERMOMETER, secret);
	}
	
	@Override
	public void setBinaryData(byte[] encrypted_data) {
		final int OUR_ENCRYPTED_LENGTH = 28;
		byte[] data = decrypt(encrypted_data, OUR_ENCRYPTED_LENGTH);
		
		// TODO Auto-generated method stub
		
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
