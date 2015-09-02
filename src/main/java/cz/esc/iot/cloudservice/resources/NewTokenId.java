package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.json.JSONException;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.oauth2.CloudToken;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.AccessToken;
import cz.esc.iot.cloudservice.persistance.model.RefreshToken;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import cz.esc.iot.cloudservice.support.ErrorJson;

public class NewTokenId extends ServerResource {

	@Post("json")
	public String auth(Representation entity) throws IOException, JSONException {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {

			// get access_token from url parameters
			Form form = getRequest().getResourceRef().getQueryAsForm();
			String id_token = form.getFirstValue("id_token");
					
			// exchange authorisation code for info about user from Google
			GoogleIdToken.Payload googleUser;

			googleUser = OAuth2.getGoogleUserFromIdToken(id_token);

			// find user in db
			UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("email").equal(googleUser.getEmail()).get();
			if (userEntity == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return gson.toJson(new ErrorJson("unregistered_user"));
			}
			
			// generate and return token
			CloudToken token = OAuth2.generateToken();

			MorfiaSetUp.getDatastore().delete(MorfiaSetUp.getDatastore().createQuery(AccessToken.class).field("user").equal(userEntity));
			MorfiaSetUp.getDatastore().delete(MorfiaSetUp.getDatastore().createQuery(RefreshToken.class).field("user").equal(userEntity));
			
			AccessToken accessToken = new AccessToken(token.getAccess_token(), userEntity);
			RefreshToken refreshToken = new RefreshToken(token.getRefresh_token(), userEntity);
			MorfiaSetUp.getDatastore().save(accessToken);
			MorfiaSetUp.getDatastore().save(refreshToken);
			
			return gson.toJson(token);
		}
		getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		return gson.toJson(new ErrorJson("invalid_request"));
	}
}
