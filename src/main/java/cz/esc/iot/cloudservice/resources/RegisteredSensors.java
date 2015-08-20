package cz.esc.iot.cloudservice.resources;

import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorAccessEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Return list of registered sensors.
 */
public class RegisteredSensors extends ServerResource {
	
	/**
	 * Identifies user and return his sensors.
	 */
	@Get("json")
	public String returnList() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		// verify user
		Form form = getRequest().getResourceRef().getQueryAsForm();
		UserEntity userEntity;
		if ((userEntity = OAuth2.verifyUser(getRequest())) == null) {
			AllSensors sensors = new AllSensors();
			List<SensorEntity> _public = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("access").equal("public").asList();
			sensors.set_public(_public);
			return gson.toJson(sensors);
		}
		
		String path = this.getRequest().getResourceRef().getPath();
		switch (path) {
		case "/registered_sensors" : return registeredSensors(gson, form, userEntity);
		default : return gson.toJson(MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(userEntity).field("uuid").equal(this.getRequestAttributes().get("uuid")).get());
		}
	}
	
	/**
	 * Reads parameters and filter result according to them.
	 */
	private String registeredSensors(Gson gson, Form form, UserEntity userEntity) {
		String hubID = null;
		String access = null;
		for (Parameter parameter : form) {
			if (parameter.getName().equals("hubID")) {
				hubID = parameter.getValue();
			} else if (parameter.getName().equals("access")) {
				access = parameter.getValue();
			}
		}

		if (access == null && hubID == null) {
			AllSensors sensors = new AllSensors();
			List<SensorEntity> my = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(userEntity).asList();
			List<SensorAccessEntity> borrowed = MorfiaSetUp.getDatastore().createQuery(SensorAccessEntity.class).field("user").equal(userEntity).asList();
			List<SensorEntity> _public = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("access").equal("public").asList();
			sensors.setMy(my);
			sensors.setBorrowed(borrowed);
			sensors.set_public(_public);
			return gson.toJson(sensors);
		} else if (access == null) {
			HubEntity hubEntity = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("user").equal(userEntity).field("uuid").equal(hubID).get();
			if (hubEntity == null) {
				return "[]";
			} else {
				return gson.toJson(MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("hub").equal(hubEntity).asList());
			}
		} else if (hubID == null) {
			if (access.equals("my")) {
				return gson.toJson(MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(userEntity).asList());
			} else if (access.equals("borrowed")) {
				return gson.toJson(MorfiaSetUp.getDatastore().createQuery(SensorAccessEntity.class).field("user").equal(userEntity).asList());
			}
		} else {
			// TODO
			return null;
		}
		return null;
	}
}
