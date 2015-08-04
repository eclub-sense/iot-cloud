package cz.esc.iot.cloudservice.messages;

import java.io.IOException;

import cz.esc.iot.cloudservice.hubs.Hub;
import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;
import cz.esc.iot.cloudservice.sensors.Sensor;

/**
 * Serves to sending messages from server to hub.
 */
public class Postman {

	public static void sendLoginAck(Hub hub) {
		try {
			hub.getSocket().getRemote().sendString("{\"type\":\"LOGIN_ACK\",\"uuid\":\"" + hub.getStringUuid() + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void reregisterAllSensors(Hub hub) throws IOException {
		for (Sensor sensor : ConnectedSensorRegistry.getInstance().getList()) {
			if (sensor.getHub().getIntUuid() == hub.getIntUuid())
				registerSensor(hub, sensor.getStringUuid(), sensor.getType().getCode());
		}
	}
	
	public static void registerSensor(Hub hub, String uuid, int sensor_type) throws IOException {
		hub.getSocket().getRemote().sendString("{\"type\":\"NEW\",\"sensor_type\":" + sensor_type + ",\"uuid\":\"" + uuid + "\"}");
	}
}
