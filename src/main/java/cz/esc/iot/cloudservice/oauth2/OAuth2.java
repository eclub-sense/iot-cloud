package cz.esc.iot.cloudservice.oauth2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
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

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.AccessToken;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import cz.esc.iot.cloudservice.unused.Checker;

/**
 * Class for communication with authorisation and token servers.
 */
public class OAuth2 {

	public static String clientID;
	public static String clientSecret;
	
	/**
	 * Sets Google's clientId and clientSecret.
	 */
	public static void setClientCredentials() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("/home/z3tt0r/google_client_credentials2")));
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
	public static UserEntity findUserInDatabase(String access_token) {
		
		AccessToken token = MorfiaSetUp.getDatastore().createQuery(AccessToken.class).field("access_token").equal(access_token).get();
		if (token == null) {
			return null;
		}
		return token.getUser();
	}
	
	
	public static GoogleIdToken.Payload getGoogleUserFromIdToken(String idToken) {
	      Checker checker = new Checker(new String[]{clientID}, clientID);
	      GoogleIdToken.Payload jwt = checker.check(idToken);
	      
	System.out.println("\nid: " + idToken);
	System.out.println("\njwt: " + jwt);
	      return jwt;
	}
	
	/**
	 * Ask for information about user. Uses received access token for it.
	 * @return Returns information from Google.
	 */
	public static GoogleUserInfo getGoogleUserFromAccessToken(String accessToken) throws IOException {
		
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
	 * @throws JSONException 
	 * @throws IOException 
	 * @throws OAuthException 
	 * @throws Exception 
	 */
	public static Token exchangeCodeForAccessToken(String code, String client_id) throws OAuthException, IOException, JSONException {

		String callback = "";
		switch(client_id) {
		case "dat":
			callback = null;
			break;
		case "adam":
			callback = "postmessage";
			break;
		case "michal":
			callback = "http://localhost:3000/callback";
			break;
		case "login":
			callback = "https://mlha-139.sin.cvut.cz:8082/callback";
		}
		
		//https://accounts.google.com/o/oauth2/token
		AccessTokenClientResource client = new AccessTokenClientResource(new Reference("https://www.googleapis.com/oauth2/v3/token"));
    	client.setClientCredentials(OAuth2.clientID, OAuth2.clientSecret);
    	OAuthParameters params = new OAuthParameters();
    	params.code(code);
    	params.grantType(GrantType.authorization_code);
    	
    	if (callback != null)
    		params.redirectURI(callback);
    	
    	Token token = client.requestToken(params);

		return token;
	}
	
	public static GoogleUserInfo getGoogleUserInfoFromCode(String code, String client_id) throws IOException, OAuthException, JSONException {
		
		// exchange code for access token
		Token token = OAuth2.exchangeCodeForAccessToken(code, client_id);
		String accessToken = token.getAccessToken();

		// get info about user from IDP
		GoogleUserInfo googleUser = OAuth2.getGoogleUserFromAccessToken(accessToken);
		
		return googleUser;
	}
	
	public static CloudToken generateToken() {		
		Random random = new Random();

        byte[] accessToken = new byte[40];
        byte[] refreshToken = new byte[40];
        random.nextBytes(accessToken);
        random.nextBytes(refreshToken);

        return new CloudToken(String.valueOf(Hex.encodeHex(accessToken)), String.valueOf(Hex.encodeHex(refreshToken)));
	}
}
