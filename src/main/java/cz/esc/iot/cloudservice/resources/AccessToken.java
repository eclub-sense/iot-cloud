package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.restlet.data.Status;
import org.restlet.ext.oauth.internal.Token;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cz.esc.iot.cloudservice.oauth2.GoogleUserInfo;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Class uses received code and exchange it for valid access token.
 */
public class AccessToken extends ServerResource {

	@Get("json")
	public String auth() {
		
		// get token
		Token token = null;
		String accessToken = null;
		try {
			token = OAuth2.getToken(getRequest());
			accessToken = token.getAccessToken();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// get info about user
		GoogleUserInfo user = null;
		try {
			user = OAuth2.getGoogleUser(accessToken);
		} catch (JsonSyntaxException | IOException e) {
			getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN);
			return "";
		}
		
		// find user in db
		UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("identifier").equal(user.getId()).get();
		// if user is not in db, register him
		if (userEntity == null)
			UserRegistrator.registerUser(user.getId(), user.getEmail());
		 
		Gson gson = new Gson();
		System.out.println(gson.toJson(token));
		return gson.toJson(token);
	}
}
