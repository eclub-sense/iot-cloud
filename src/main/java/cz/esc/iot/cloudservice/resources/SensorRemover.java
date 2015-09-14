package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.messages.Postman;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import cz.esc.iot.cloudservice.support.WebSocketRegistry;


public class SensorRemover  extends ServerResource {
	
	@Delete
	public void acceptRepresentation(Representation entity) throws IOException {
		if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
			
			String json = entity.getText();
			
			// get access_token from url parameters
			Form form = getRequest().getResourceRef().getQueryAsForm();
			String access_token = form.getFirstValue("access_token");
			if (access_token == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				return;
			}
				
			// verify user
			UserEntity user;
			if ((user = OAuth2.findUserInDatabase(access_token)) == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				return;
			}
			
			// create JSONObject
    		JSONObject jsonObject;
    		String uuid = null;
    		try {
    			jsonObject = new JSONObject(json);
    			uuid = (String)jsonObject.get("uuid");
    		} catch (NullPointerException | JSONException e) {
    			System.out.println(e.getMessage());
    		}
			
			SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("uuid").equal(uuid).get();

			if((sensor != null) && (!sensor.getUser().getId().equals(user.getId()))) {
				getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				return;
			} else if(sensor == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
				return;
			}
			
			// delete sensor from database
			MorfiaSetUp.getDatastore().delete(sensor);
			
			WebSocket socket = WebSocketRegistry.get(sensor.getUuid());
			
			// send message to hub
			Postman.deleteSensor(socket, sensor);
		}
	}
}
