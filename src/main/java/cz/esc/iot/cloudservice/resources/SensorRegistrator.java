package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.messages.Postman;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.MeasuredValues;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import cz.esc.iot.cloudservice.registry.WebSocketRegistry;

/**
 * Register new sensor.
 */
public class SensorRegistrator extends ServerResource {
	
	@Post
	public void acceptRepresentation(Representation entity) throws IOException {
	    if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
	    	String username = getRequest().getChallengeResponse().getPrincipal().getName();
    		String json = entity.getText();
    		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    		SensorEntity sensor = gson.fromJson(json, SensorEntity.class);
    		MeasuredValues values = new MeasuredValues(null, sensor, null);
    		List<HubEntity> hubs = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(username).get().getHubEntities();
    		WebSocket socket;
			try {
				socket = chooseHubUuid(hubs);
				MorfiaSetUp.getDatastore().save(sensor);
				MorfiaSetUp.getDatastore().save(values);
				HubEntity hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("uuid").equal(socket.getHubUuid()).get();
				UserEntity user = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(username).get();
				MorfiaSetUp.getDatastore().update(hub, MorfiaSetUp.getDatastore().createUpdateOperations(HubEntity.class).add("sensorEntities", sensor, true));
				MorfiaSetUp.getDatastore().update(user, MorfiaSetUp.getDatastore().createUpdateOperations(UserEntity.class).add("sensorEntities", sensor, true));
				List<SensorEntity> list = new LinkedList<SensorEntity>();
				list.add(sensor);
				Postman.registerSensors(socket, list);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	/**
	 * Chooses random hub with which new sensor will be associated.
	 * @throws Exception 
	 */
	private WebSocket chooseHubUuid(List<HubEntity> hubs) throws Exception {
		HubEntity[] entities = new HubEntity[hubs.size()];
		entities = hubs.toArray(entities);
		Random randomGenerator = new Random();
		if (WebSocketRegistry.size() == 0) throw new Exception("No hub is connected. Can not connect new sensor anywhere.");
		int random = randomGenerator.nextInt(WebSocketRegistry.size());
		int first = random;
		while (WebSocketRegistry.get(entities[random].getUuid()) == null) {
			random = (random +1) % (WebSocketRegistry.size());
			if (random == first) throw new Exception("No hub is connected. Can not connect new sensor anywhere.");
		}
		return WebSocketRegistry.get(entities[random].getUuid());
	}
}
