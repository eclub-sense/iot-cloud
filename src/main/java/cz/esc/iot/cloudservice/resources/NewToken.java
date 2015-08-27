package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.json.JSONException;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.oauth2.AccessTokenRequest;
import cz.esc.iot.cloudservice.oauth2.GoogleUserInfo;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.oauth2.CloudToken;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Class uses received code and exchange it for valid access token.
 */
public class NewToken extends ServerResource {

	@Post("json")
	public String auth(Representation entity) throws IOException, JSONException {

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		AccessTokenRequest request = gson.fromJson(entity.getText(), AccessTokenRequest.class);
		
		if (!request.getGrant_type().equals("authorization_code"))
			return "{\n\"error\":\"Invalid grant type.\",\n\"code\":2\n}";
		
		if (!request.getClient_id().equals("abeceda"))
			return "{\n\"error\":\"Invalid client id.\",\n\"code\":3\n}";
		
		// exchange authorisation code for info about user from Google
		GoogleUserInfo googleUser;
		try {
			googleUser = OAuth2.getGoogleUserInfoFromCode(request.getCode());
		} catch (OAuthException e) {
			return "{\n\"error\":\"Invalid code.\",\n\"code\":1\n}";
		}
		
		// find user in db
		UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("email").equal(googleUser.getEmail()).get();
		if (userEntity == null)
			return "{\n\"error\":\"User " + googleUser.getEmail() + " is not registered.\",\n\"code\":4\n}";
		
		CloudToken token = OAuth2.generateToken();
		return gson.toJson(token);
	}
}
