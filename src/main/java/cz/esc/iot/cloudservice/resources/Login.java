package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.json.JSONException;
import org.restlet.data.Form;
import org.restlet.ext.oauth.AccessTokenServerResource;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.resource.Get;

import cz.esc.iot.cloudservice.oauth2.GoogleUserInfo;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Class uses received code and exchange it for valid access token.
 */
public class Login extends AccessTokenServerResource {

	@Get("json")
	public String auth() throws IOException, OAuthException, JSONException {

		// exchange authorisation code for info about user from Google
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String code = form.getFirstValue("code");
		if (code == null)
			return "{\n\"error\":\"Authorisation code required.\",\n\"code\":1\n}";
		
		GoogleUserInfo googleUser = OAuth2.getGoogleUserInfoFromCode(code);
		
		// find user in db
		UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("email").equal(googleUser.getEmail()).get();
		if (userEntity == null)
			return "{\n\"error\":\"User " + googleUser.getEmail() + " is not registered.\",\n\"code\":2\n}";
		
		return "access token";
	}
}
