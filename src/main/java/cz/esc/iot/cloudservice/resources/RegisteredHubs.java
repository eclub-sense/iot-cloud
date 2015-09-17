package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import cz.esc.iot.cloudservice.support.WebSocketRegistry;

/**
 * Return list of registered hubs. Only hubs owned by signed in user are returned.
 */
public class RegisteredHubs extends ServerResource {

	/**
	 * Deletes registered hub.
	 * @throws IOException
	 */
	@Delete
	public void acceptRepresentation(Representation entity) throws IOException {
		
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

		String uuid = (String)this.getRequestAttributes().get("uuid");
		
		// finds sensor in database
		HubEntity hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("uuid").equal(uuid).get();
		
		if ((hub != null) && (!hub.getUser().getId().equals(user.getId()))) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return;
		} else if (hub == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return;
		}
		
		// delete sensor from database
		MorfiaSetUp.getDatastore().delete(hub);
		
		// delete hub's websocket from registry
		WebSocketRegistry.remove(hub.getUuid());
	}

	/**
	 * @return Returns list of registered hubs.
	 */
	@Get("json")
	public String returnList() {
		
		// get access_token from url parameters
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String access_token = form.getFirstValue("access_token");
		if (access_token == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return "";
		}
			
		// verify user
		UserEntity user;
		if ((user = OAuth2.findUserInDatabase(access_token)) == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
			return "";
		}
		
		String path = this.getRequest().getResourceRef().getPath();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		switch (path) {
		// returns all user's hubs
		case "/registered_hubs" : 
			List<HubEntity> hubs = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("user").equal(user).asList();
			if (hubs.size() == 0)
				return "no hub";
			else
				return gson.toJson(hubs);
		// returns only hub specified by its uuid
		default : return gson.toJson(MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("user").equal(user).field("uuid").equal(this.getRequestAttributes().get("uuid")).get());
		}
	}
}
