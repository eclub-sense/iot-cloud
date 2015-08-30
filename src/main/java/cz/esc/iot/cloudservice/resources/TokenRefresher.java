package cz.esc.iot.cloudservice.resources;

import java.util.Date;

import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.oauth2.CloudToken;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.AccessToken;
import cz.esc.iot.cloudservice.persistance.model.RefreshToken;

/**
 * Refreshes access token when retrieving valid refresh token.
 */
public class TokenRefresher extends ServerResource {

	@Get("json")
	public String refresh_token() {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String refresh_token = form.getFirstValue("refresh_token");
		String grant_type = form.getFirstValue("grant_type");
		if (refresh_token == null || grant_type == null ) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return "{\n\"error\":\"invalid_request\"\n}";
		} else if (!grant_type.equals("refresh_token")) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return "{\n\"error\":\"unsupported_grant_type\"\n}";
		}
		
		RefreshToken refresh = MorfiaSetUp.getDatastore().find(RefreshToken.class).field("refresh_token").equal(refresh_token).get();
		if(refresh == null) {
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
			return "{\n\"error\":\"invalid_grant\"\n}";
		}
		
		// generate new token
		CloudToken token = OAuth2.generateToken();
		
		// delete possible access token in database
		MorfiaSetUp.getDatastore().delete(MorfiaSetUp.getDatastore().createQuery(AccessToken.class).field("user").equal(refresh.getUser()));
		
		// update refresh token in database...
		int newCount = refresh.getRefreshCounter() + 1;
		// ...if token has been refreshed less than 10 times,...
		if (newCount < 2)
			MorfiaSetUp.getDatastore().update(refresh, MorfiaSetUp.getDatastore().createUpdateOperations(RefreshToken.class).set("refreshCounter", newCount).set("refresh_token", token.getRefresh_token()));
		// ...else, refresh token is deleted from database
		else
			MorfiaSetUp.getDatastore().delete(MorfiaSetUp.getDatastore().createQuery(RefreshToken.class).field("user").equal(refresh.getUser()));
		
		// save new access token to database
		MorfiaSetUp.getDatastore().save(new AccessToken(token.getAccess_token(), refresh.getUser()));
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(token);
	}
}
