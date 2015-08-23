package cz.esc.iot.cloudservice.messages;

import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;

/**
 * Serves to sending messages from server to hub.
 */
public class Postman {

	/**
	 * When user is successfully verified after LOGIN message received from hub,
	 * LOGIN_ACK message is send from zettor. 
	 */
	public static void sendLoginAck(WebSocket socket, String uuid) {
		try {
			socket.getRemote().sendString("{\"type\":\"LOGIN_ACK\",\"uuid\":\"" + uuid + "\"}");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * When hub connects and it's uuid is already in database, all sensors associated with this hub
	 * in database are registered to it again.
	 */
	public static void reregisterAllSensors(WebSocket socket, String uuid) throws IOException {
		HubEntity hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("uuid").equal(uuid).get();
		List<SensorEntity> sensors = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("hub").equal(hub).asList();
		for (SensorEntity sensor : sensors) {
			registerSensor(socket, sensor);
		}
	}

	/**
	 * Zettor sends NEW message to hub.
	 */
	public static void registerSensor(WebSocket socket, SensorEntity sensor) throws IOException {
		HubNewMsg msg = new HubNewMsg(sensor.getUuid(), sensor.getType());
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		socket.getRemote().sendString(gson.toJson(msg));
		System.out.println(msg);
		System.out.println(gson.toJson(msg));
	}
}
