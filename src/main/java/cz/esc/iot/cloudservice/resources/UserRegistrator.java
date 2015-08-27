package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.json.JSONException;
import org.restlet.data.Form;
import org.restlet.ext.oauth.AccessTokenServerResource;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.oauth2.GoogleUserInfo;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * New UserEntity object is stored into database.
 */
public class UserRegistrator extends AccessTokenServerResource {
	
	@Post("json")
	public String newUser(Representation entity) throws IOException, OAuthException, JSONException {
		
		// exchange authorisation code for info about user from Google
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String code = form.getFirstValue("code");
		if (code == null)
			return "{\n\"error\":\"Authorisation code required.\",\n\"code\":1\n}";
		
		GoogleUserInfo googleUser = OAuth2.getGoogleUserInfoFromCode(code);

		
		// register user
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		UserEntity newUser = gson.fromJson(entity.getText(), UserEntity.class);
		newUser.setEmail(googleUser.getEmail());
		MorfiaSetUp.getDatastore().save(newUser);
		
		return "access token";
	}
}
