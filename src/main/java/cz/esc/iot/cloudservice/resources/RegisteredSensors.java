package cz.esc.iot.cloudservice.resources;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Return list of registered sensors.
 */
public class RegisteredSensors extends ServerResource {
	
	@Get("json")
	public String returnList() {
		String user = this.getRequest().getChallengeResponse().getIdentifier();
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String path = this.getRequest().getResourceRef().getPath();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		switch (path) {
		case "/registered_sensors" : return registeredSensors(gson, form, user);
		default : UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(user).get();
				  return gson.toJson(MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(userEntity).field("uuid").equal(this.getRequestAttributes().get("uuid")).get());
		}
	}
	
	private String registeredSensors(Gson gson, Form form, String user) {
		int type = -1;
		for (Parameter parameter : form) {
			if (parameter.getName().equals("type")) {
				type = Integer.parseInt(parameter.getValue());
			}
		}
		
		if (type == -1) {
			return gson.toJson(MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(user).get().getSensorEntities());
		} else {
			UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(user).get();
			return gson.toJson(MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(userEntity).field("type").equal(type).asList());
		}
	}
}
