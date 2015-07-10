package cz.esc.iot.cloudservice.sensors;

import cz.esc.iot.cloudservice.sensors.data.ESCThermometerData;

public class ESCThermometer extends Sensor {

	private ESCThermometerData data;
	
	public ESCThermometer(int uuid, int secret) {
		super(uuid, SensorType.THERMOMETER, secret);
	}
}
