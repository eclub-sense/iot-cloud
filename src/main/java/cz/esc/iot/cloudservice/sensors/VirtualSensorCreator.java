package cz.esc.iot.cloudservice.sensors;

public class VirtualSensorCreator {

	public static Sensor createSensorInstance(int uuid, SensorType type, int secret) {
		switch (type) {
		case THERMOMETER : return new ESCThermometer(uuid, secret);
		default : return null;
		}
	}

}
