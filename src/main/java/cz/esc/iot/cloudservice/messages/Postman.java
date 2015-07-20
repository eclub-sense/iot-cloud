package cz.esc.iot.cloudservice.messages;

import java.io.IOException;

import cz.esc.iot.cloudservice.hubs.Hub;
import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;
import cz.esc.iot.cloudservice.sensors.Sensor;

public class Postman {

	public static void sendLoginAck(Hub hub) {
		try {
			System.out.println(hub);
			System.out.println(hub.getSocket());
			String uuid = String.format("%08d", hub.getIntUuid());
			System.out.println(uuid);
			hub.getSocket().getRemote().sendString("{\"type\":\"LOGIN_ACK\",\"uuid\":" + uuid + "}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void reregisterAllSensors(Hub hub) throws IOException {
		for (Sensor sensor : ConnectedSensorRegistry.getInstance().getList()) {
			if (sensor.getHub().getIntUuid() == hub.getIntUuid())
				registerSensor(hub, sensor.getStringUuid());
		}
	}
	
	public static void registerSensor(Hub hub, String uuid) throws IOException {
		hub.getSocket().getRemote().sendString("{\"type\":\"NEW\",\"uuid\":" + uuid + "}");
	}
}
