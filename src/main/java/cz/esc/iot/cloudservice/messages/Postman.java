package cz.esc.iot.cloudservice.messages;

import java.io.IOException;
import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.hubs.Hub;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;
import cz.esc.iot.cloudservice.sensors.Sensor;

/**
 * Serves to sending messages from server to hub.
 */
public class Postman {

	public static void sendLoginAck(WebSocket socket, String uuid) {
		try {
			socket.getRemote().sendString("{\"type\":\"LOGIN_ACK\",\"uuid\":\"" + uuid + "\"}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void reregisterAllSensors(WebSocket socket, String uuid) throws IOException {
		HubEntity hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("uuid").equal(uuid).get();
		Collection<SensorEntity> sensors = hub.getSensorEntities();
		registerSensors(socket, sensors);
	}
	
	public static void registerSensors(WebSocket socket, Collection<SensorEntity> sensors) throws IOException {
		HubNewMsg msg = new HubNewMsg(sensors);
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		socket.getRemote().sendString(gson.toJson(msg));
	}
}
