package cz.esc.iot.cloudservice.resources;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Return list of registered hubs.
 */
public class RegisteredHubs extends ServerResource {

	@Get("json")
	public String returnList() {
		String user = this.getRequest().getChallengeResponse().getIdentifier();
		String path = this.getRequest().getResourceRef().getPath();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		switch (path) {
		case "/registered_hubs" : return gson.toJson(MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(user).get().getHubEntities());
		default : UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(user).get();
		  return gson.toJson(MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("user").equal(userEntity).field("uuid").equal(this.getRequestAttributes().get("uuid")).get());
		}
	}
}
