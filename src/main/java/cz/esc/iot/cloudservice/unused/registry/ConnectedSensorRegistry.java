package cz.esc.iot.cloudservice.unused.registry;

import cz.esc.iot.cloudservice.unused.sensors.Sensor;

public class ConnectedSensorRegistry extends ConnectedDeviceList<Sensor> {

	private static ConnectedSensorRegistry list = new ConnectedSensorRegistry();
	
	public static ConnectedSensorRegistry getInstance() {
		return list;
	}
}
