package cz.esc.iot.cloudservice.registry;

import cz.esc.iot.cloudservice.sensors.Sensor;

public class ConnectedSensorRegistry extends ConnectedDeviceList<Sensor> {

	private static ConnectedSensorRegistry list = new ConnectedSensorRegistry();
	
	public static ConnectedSensorRegistry getInstance() {
		return list;
	}
}
