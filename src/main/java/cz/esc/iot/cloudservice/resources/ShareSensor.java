package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.SensorAccessEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Share sensor according to parameter access=public/protected.
 * 
 * @author martin
 */
public class ShareSensor extends ServerResource {

	@Post
	public void acceptRepresentation(Representation entity) throws IOException {
		if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {

	    	String ownername = getRequest().getChallengeResponse().getPrincipal().getName();
    		String json = entity.getText();
    		
    		JSONObject jsonObject;
    		String uuid = null;
    		String username = null;
    		String permission = null;
    		String access = null;
    		try {
    			jsonObject = new JSONObject(json);
    			uuid = (String)jsonObject.get("uuid");
    			access  = (String)jsonObject.get("access");
    			username = (String)jsonObject.get("username");
    			permission = (String)jsonObject.get("permission");
    		} catch (NullPointerException | JSONException e) {
    			System.out.println(e.getMessage());
    		}

    		System.out.println(access);
    		if (access.equals("protected")) {
	    		UserEntity owner = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(ownername).get();
	    		SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(owner).field("uuid").equal(uuid).get();
	    		UserEntity user = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(username).get();
	    		if (sensor != null && user != null) {
	    			MorfiaSetUp.getDatastore().update(sensor, MorfiaSetUp.getDatastore().createUpdateOperations(SensorEntity.class).set("access", "protected"));
	    			SensorAccessEntity accessEntity = new SensorAccessEntity(owner, user, permission, sensor);
	    			MorfiaSetUp.getDatastore().save(accessEntity);
	    		}
    		} else if (access.equals("public")) {
    			System.out.println("A");
    			UserEntity owner = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(ownername).get();
    			System.out.println("B");
    			SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(owner).field("uuid").equal(uuid).get();
	    		if (sensor != null) {
	    			System.out.println("C");
	    			MorfiaSetUp.getDatastore().update(sensor, MorfiaSetUp.getDatastore().createUpdateOperations(SensorEntity.class).set("access", "public"));
	    		}
    		}
		}
	}
}
