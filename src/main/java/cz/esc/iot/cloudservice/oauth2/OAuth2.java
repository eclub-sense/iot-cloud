package cz.esc.iot.cloudservice.oauth2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONException;
import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Reference;
import org.restlet.ext.oauth.AccessTokenClientResource;
import org.restlet.ext.oauth.GrantType;
import org.restlet.ext.oauth.OAuthException;
import org.restlet.ext.oauth.OAuthParameters;
import org.restlet.ext.oauth.internal.Token;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

public class OAuth2 {

	public static String clientID;
	public static String clientSecret;
	
	public static void setCredentials() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("/usr/etc/google_client_credentials")));
			clientID = br.readLine();
			clientSecret = br.readLine();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static UserEntity verifyUser(Request req) {
		Form form = req.getResourceRef().getQueryAsForm();
		String accessToken = form.getFirstValue("access_token");
		if (accessToken == null) {
			return null;
		}
		GoogleUserInfo googleUser = null;
		try {
			googleUser = OAuth2.getGoogleUser(accessToken);
		} catch (JsonSyntaxException | IOException e1) {
			return null;
		}
		UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("identifier").equal(googleUser.getId()).get();
		if (userEntity == null) {
			return null;
		}
		return userEntity;
	}
	
	public static GoogleUserInfo getGoogleUser(String accessToken) throws JsonSyntaxException, IOException {
		
		/*
		 * Ask for info about user.
		 */
		String uri = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
		ClientResource getter = new ClientResource(uri);
		Representation response = getter.get();
	
		Gson gson = new Gson();
		GoogleUserInfo user = null;

		user = gson.fromJson(response.getText(), GoogleUserInfo.class);

		return user;
	}
	
	public static Token getToken(Request req) throws IOException {
		
		/*
		 * Ask for access token.
		 */
		Form form = req.getResourceRef().getQueryAsForm();
		AccessTokenClientResource client = new AccessTokenClientResource(new Reference("https://accounts.google.com/o/oauth2/token"));
    	client.setClientCredentials(OAuth2.clientID, OAuth2.clientSecret);
    	OAuthParameters params = new OAuthParameters();
    	params.code(form.getFirstValue("code"));
    	params.redirectURI("http://localhost:8080/callback");
    	params.grantType(GrantType.authorization_code);
    	
    	Token token = null;
		try {
			token = client.requestToken(params);
		} catch (OAuthException | IOException | JSONException e) {
			e.printStackTrace();
		}
		return token;
	}
}