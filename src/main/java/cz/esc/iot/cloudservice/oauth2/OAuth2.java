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

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.gson.Gson;

import com.google.gson.JsonSyntaxException;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Class for communication with authorisation and token servers.
 */
public class OAuth2 {

	public static String clientID;
	public static String clientSecret;
	
	/**
	 * Sets Google's clientId and clientSecret.
	 */
	public static void setCredentials() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("/home/z3tt0r/google_client_credentials")));
			clientID = br.readLine();
			clientSecret = br.readLine();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Find, whether user obtained from Google is registered in
	 * Zettor's database.
	 * @return Returns verified user.
	 */
	public static UserEntity findUserInDatabase(Request req) {
		Form form = req.getResourceRef().getQueryAsForm();
		String accessToken = form.getFirstValue("access_token");
		
		GoogleUserInfo googleUser = null;
		try {
			if (accessToken != null) {
				googleUser = OAuth2.getGoogleUserFromAccessToken(accessToken);
			} else
				return null;
		} catch (JsonSyntaxException | IOException e1) {
			return null;
		}
System.out.println("email: " + googleUser.getEmail());
		UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("emails").contains(googleUser.getEmail()).get();
		if (userEntity == null) {
			// google user is not in zettor's database
			return null; //UserRegistrator.registerUser(id, email);
		}
		return userEntity;
	}
	
	/**
	 * Ask for information about user. Uses received access token for it.
	 * @return Returns information from Google.
	 */
	public static GoogleUserInfo getGoogleUserFromAccessToken(String accessToken) throws JsonSyntaxException, IOException {
		
		String uri = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
		ClientResource getter = new ClientResource(uri);
		Representation response = getter.get();
	
		Gson gson = new Gson();
		GoogleUserInfo user = null;

		user = gson.fromJson(response.getText(), GoogleUserInfo.class);

		return user;
	}
	
	/**
	 * Asks Google for access token. Uses code, received as parameter, for it.
	 * @return Returns valid access token.
	 */
	public static Token getToken(Request req) throws IOException {
		
		Form form = req.getResourceRef().getQueryAsForm();
		AccessTokenClientResource client = new AccessTokenClientResource(new Reference("https://accounts.google.com/o/oauth2/token"));
    	client.setClientCredentials(OAuth2.clientID, OAuth2.clientSecret);
    	OAuthParameters params = new OAuthParameters();
    	params.code(form.getFirstValue("code"));
    	params.redirectURI("http://mlha-139.sin.cvut.cz:8080/callback");
    	params.grantType(GrantType.authorization_code);
    	System.out.println("client: "+client);
    	System.out.println("code: "+form.getFirstValue("code"));
    	Token token = null;
		try {
			token = client.requestToken(params);
			System.out.println("token: "+ token);
		} catch (OAuthException | IOException | JSONException e) {
			e.printStackTrace();
		}
		return token;
	}
}
