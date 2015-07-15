package cz.esc.iot.cloudservice.sensors;

public class ESCThermometer extends Sensor {
	
	public ESCThermometer() {
		super();
	}
	
	public ESCThermometer(int uuid, int secret) {
		super(uuid, SensorType.THERMOMETER, secret);
	}
}
