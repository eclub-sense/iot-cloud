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
import cz.esc.iot.cloudservice.support.UserRegistrator;

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
	public static UserEntity verifyUser(Request req) {
		Form form = req.getResourceRef().getQueryAsForm();
		String accessToken = form.getFirstValue("access_token");
		//String idToken = form.getFirstValue("id_token");
		
		Object googleUser = null;
		String id = null;
		String email;
		try {
			/*if (idToken != null) {
				googleUser = OAuth2.getGoogleUserFromIDToken(idToken);
				email = ((GoogleIdToken.Payload)googleUser).getEmail();
				id = ((GoogleIdToken.Payload)googleUser).getUserId();
			} else*/ if (accessToken != null) {
				googleUser = OAuth2.getGoogleUserFromAccessToken(accessToken);
				email = ((GoogleUserInfo)googleUser).getEmail();
				id = ((GoogleUserInfo)googleUser).getId();
			} else
				return null;
		} catch (JsonSyntaxException | IOException e1) {
			return null;
		}
		System.out.println("email: " + email);
		UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("emails").contains(email).get();
		if (userEntity == null) {
			UserRegistrator.registerUser(id, email);
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
	
	public static GoogleIdToken.Payload getGoogleUserFromIDToken(String idToken) {
//System.out.println(idToken);
//System.out.println(clientID);
	      Checker checker = new Checker(new String[]{clientID}, clientID);
	      GoogleIdToken.Payload jwt = checker.check(idToken);
	      //Gson gson = new Gson();
	System.out.println("\n\njwt: " + jwt + "\n");
	//System.out.println(gson.toJson(jwt));

	      return jwt;
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
    	
    	Token token = null;
		try {
			token = client.requestToken(params);
		} catch (OAuthException | IOException | JSONException e) {
			e.printStackTrace();
		}
		return token;
	}
}
