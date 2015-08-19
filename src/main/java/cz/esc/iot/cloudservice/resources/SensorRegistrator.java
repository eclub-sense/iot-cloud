package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.messages.Postman;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
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
			
			// verify user
			UserEntity user;
			if ((user = OAuth2.verifyUser(getRequest())) == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN);
				return;
			}
			
    		String json = entity.getText();
    		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    		SensorEntity sensor = gson.fromJson(json, SensorEntity.class);
    		sensor.setData(null);
    		sensor.setAccess("private");
    		List<HubEntity> hubs = user.getHubEntities();
    		WebSocket socket;
    		HubEntity hub;
    		
    		// create JSONObject
    		JSONObject jsonObject;
    		Object hub_uuid = null;
    		try {
    			jsonObject = new JSONObject(json);
    			hub_uuid = (String)jsonObject.get("hub_uuid");
    		} catch (NullPointerException | JSONException e) {
    			System.out.println(e.getMessage());
    		}
    		
			try {
				// if hub_id == null, hub which will be associated with registered sensor has to be chosen.
				if (hub_uuid == null) {
					hub = chooseHubUuid(hubs);
					socket = WebSocketRegistry.get(hub.getUuid());
				// hub has been chosen by user
				} else {
					hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("uuid").equal(hub_uuid).get();
					socket = WebSocketRegistry.getCloudSocket();
				}
				//socket = WebSocketRegistry.get(hub.getUuid());
				
				sensor.setHub(hub);
				sensor.setUser(user);
				MorfiaSetUp.getDatastore().save(sensor);
				MorfiaSetUp.getDatastore().update(hub, MorfiaSetUp.getDatastore().createUpdateOperations(HubEntity.class).add("sensorEntities", sensor, true));
				MorfiaSetUp.getDatastore().update(user, MorfiaSetUp.getDatastore().createUpdateOperations(UserEntity.class).add("sensorEntities", sensor, true));
				if (socket != null)
					Postman.registerSensor(socket, sensor);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	/**
	 * Chooses random hub with which new sensor will be associated.
	 * @throws Exception 
	 */
	private HubEntity chooseHubUuid(List<HubEntity> hubs) throws Exception {
		HubEntity[] entities = new HubEntity[hubs.size()];
		entities = hubs.toArray(entities);
		Random randomGenerator = new Random();
		if (hubs.size() == 0) throw new Exception("No hub is connected. Can not connect new sensor anywhere.");
		int random = randomGenerator.nextInt(hubs.size());
		int first = random;
		while (WebSocketRegistry.get(entities[random].getUuid()) == null) {
			random = (random +1) % (WebSocketRegistry.size());
			if (random == first) throw new Exception("No hub is connected. Can not connect new sensor anywhere.");
		}
		return entities[random];
	}
}
