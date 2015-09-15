package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.SensorAccessEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Share sensor according to parameter access=public/protected.
 */
public class ShareSensor extends ServerResource {

	/**
	 * Accepts json from owner in format: {"uuid":string, "access":"protected", "email": string, "permission":"read"/"write"}
	 * for protected sensor and in format: {"uuid":string, "access":"public"} for public.
	 */
	@Post
	public void acceptRepresentation(Representation entity) throws IOException {
		if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {

			// get access_token from url parameters
			Form form = getRequest().getResourceRef().getQueryAsForm();
			String access_token = form.getFirstValue("access_token");
			if (access_token == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				return;
			}
				
			// verify user
			UserEntity owner;
			if ((owner = OAuth2.findUserInDatabase(access_token)) == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
				return;
			}
			
    		String json = entity.getText();
    		
    		// parse parts from json
    		JSONObject jsonObject;
    		String uuid = null;
    		String email = null;
    		String permission = null;
    		String access = null;
    		try {
    			jsonObject = new JSONObject(json);
    			uuid = (String)jsonObject.get("uuid");
    			access  = (String)jsonObject.get("access");
    			email = (String)jsonObject.get("email");
    			permission = (String)jsonObject.get("permission");
    		} catch (NullPointerException | JSONException e) {
    			System.out.println(e.getMessage());
    		}
    		
    		// protected mode
    		if (access.equals("protected")) {
	    		SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(owner).field("uuid").equal(uuid).get();
	    		UserEntity user = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("email").equal(email).get();
	    		if (sensor != null && user != null) {
	    			MorfiaSetUp.getDatastore().update(sensor, MorfiaSetUp.getDatastore().createUpdateOperations(SensorEntity.class).set("access", "protected"));
	    			SensorAccessEntity accessEntity = new SensorAccessEntity(owner, user, permission, sensor);
	    			MorfiaSetUp.getDatastore().save(accessEntity);
	    		}
	    	// public mode
    		} else if (access.equals("public")) {
    			SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(owner).field("uuid").equal(uuid).get();
	    		
    			if (sensor != null) {
	    			if (sensor.getAccess().equals("protected"))
	    				MorfiaSetUp.getDatastore().delete(MorfiaSetUp.getDatastore().find(SensorAccessEntity.class).field("sensor").equal(sensor));
	    			MorfiaSetUp.getDatastore().update(sensor, MorfiaSetUp.getDatastore().createUpdateOperations(SensorEntity.class).set("access", "public"));
	    		}
    		}
		}
	}
}
