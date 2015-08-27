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
import cz.esc.iot.cloudservice.oauth2.CloudToken;
import cz.esc.iot.cloudservice.oauth2.GoogleUserInfo;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * New UserEntity object is stored into database.
 */
public class UserRegistrator extends ServerResource {
	
	@Post("json")
	public String newUser(Representation entity) throws IOException, OAuthException, JSONException {
		System.out.println(entity.getText());
		System.out.println(getRequest().getEntityAsText());
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		AccessTokenRequest request = gson.fromJson(entity.getText(), AccessTokenRequest.class);
		System.out.println(request);
		if (request.getGrant_type() == null || !request.getGrant_type().equals("authorization_code"))
			return "{\n\"error\":\"Invalid grant type.\",\n\"code\":2\n}";
		
		if (request.getClient_id() == null || !request.getClient_id().equals("abeceda"))
			return "{\n\"error\":\"Invalid client id.\",\n\"code\":3\n}";
		
		if (request.getCode() == null)
			return "{\n\"error\":\"No code received.\",\n\"code\":3\n}";
				
		// exchange authorisation code for info about user from Google
		GoogleUserInfo googleUser;
		try {
			googleUser = OAuth2.getGoogleUserInfoFromCode(request.getCode());
		} catch (OAuthException e) {
			return "{\n\"error\":\"Invalid code.\",\n\"code\":1\n}";
		}

		// register user
		UserEntity newUser = gson.fromJson(entity.getText(), UserEntity.class);
		newUser.setEmail(googleUser.getEmail());
		MorfiaSetUp.getDatastore().save(newUser);
		
		CloudToken token = OAuth2.generateToken();
		return gson.toJson(token);
	}
}
