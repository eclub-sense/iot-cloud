package cz.esc.iot.cloudservice.resources;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Return list of registered hubs.
 */
public class RegisteredHubs extends ServerResource {

	@Get("json")
	public String returnList() {
		
		// verify user
		UserEntity user;
		if ((user = OAuth2.verifyUser(getRequest())) == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN);
			return null;
		}
		
		String path = this.getRequest().getResourceRef().getPath();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		switch (path) {
		case "/registered_hubs" : return gson.toJson(MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("user").equal(user).asList());
		default : return gson.toJson(MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("user").equal(user).field("uuid").equal(this.getRequestAttributes().get("uuid")).get());
		}
	}
}
