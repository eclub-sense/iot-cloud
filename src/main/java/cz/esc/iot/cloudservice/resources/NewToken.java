package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.util.Date;

import org.json.JSONException;
import org.restlet.data.MediaType;
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
import cz.esc.iot.cloudservice.persistance.model.AccessToken;
import cz.esc.iot.cloudservice.persistance.model.RefreshToken;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

/**
 * Class uses received code and exchange it for valid access token.
 */
public class NewToken extends ServerResource {

	@Post("json")
	public String auth(Representation entity) throws IOException, JSONException {
		if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
			String json = entity.getText();
			
			Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
			AccessTokenRequest request = gson.fromJson(json, AccessTokenRequest.class);

			// control request arguments
			if (request.getGrant_type() == null || request.getClient_id() == null || request.getCode() == null)
				return "{\n\"error\":\"invalid_request\"}";
			else if (!request.getGrant_type().equals("authorization_code"))
				return "{\n\"error\":\"unsupported_grant_type\"\n}";
			else if (!request.getClient_id().equals("abeceda"))
				return "{\n\"error\":\"invalid_client\"\n}";
					
			// exchange authorisation code for info about user from Google
			GoogleUserInfo googleUser;
			try {
				googleUser = OAuth2.getGoogleUserInfoFromCode(request.getCode());
			} catch (OAuthException e) {
				return "{\n\"error\":\"invalid_grant\"\n}";
			}
			
			// find user in db
			UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("email").equal(googleUser.getEmail()).get();
			if (userEntity == null)
				return "{\n\"error\":\"User " + googleUser.getEmail() + " is not registered.\",\n\"code\":4\n}";
			
			// generate and return token
			CloudToken token = OAuth2.generateToken();

			MorfiaSetUp.getDatastore().delete(MorfiaSetUp.getDatastore().createQuery(AccessToken.class).field("user").equal(userEntity));
			MorfiaSetUp.getDatastore().delete(MorfiaSetUp.getDatastore().createQuery(RefreshToken.class).field("user").equal(userEntity));
			
			AccessToken accessToken = new AccessToken(token.getAccess_token(), new Date());
			RefreshToken refreshToken = new RefreshToken(token.getRefresh_token(), new Date());
			MorfiaSetUp.getDatastore().save(accessToken);
			MorfiaSetUp.getDatastore().save(refreshToken);
			
			return gson.toJson(token);
		}
		return "{\n\"error\":\"invalid_request\"\n}";
	}
}
