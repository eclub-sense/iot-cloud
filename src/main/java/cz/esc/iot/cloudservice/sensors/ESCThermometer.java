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
	public void setBinaryData(byte[] data) {
		// TODO Auto-generated method stub
		
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getPressure() {
		return pressure;
	}

	public void setPressure(int pressure) {
		this.pressure = pressure;
	}
}
