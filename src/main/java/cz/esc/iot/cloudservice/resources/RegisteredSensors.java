package cz.esc.iot.cloudservice.resources;

import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorAccessEntity;
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
		String hubID = null;
		String access = null;
		for (Parameter parameter : form) {
			if (parameter.getName().equals("hubID")) {
				hubID = parameter.getValue();
			} else if (parameter.getName().equals("access")) {
				access = parameter.getValue();
			}
		}
		UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(user).get();

		if (access == null && hubID == null) {
			AllSensors sensors = new AllSensors();
			List<SensorEntity> my = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("user").equal(userEntity).asList();
			List<SensorAccessEntity> borrowed = MorfiaSetUp.getDatastore().createQuery(SensorAccessEntity.class).field("user").equal(userEntity).asList();
			sensors.setMy(my);
			sensors.setBorrowed(borrowed);
			return gson.toJson(sensors);
		} else if (access == null) {
			HubEntity hubEntity = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("user").equal(userEntity).field("uuid").equal(hubID).get();
			if (hubEntity == null) {
				return "[]";
			} else {
				return gson.toJson(hubEntity.getSensorEntities());
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
