package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.json.JSONException;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
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
import cz.esc.iot.cloudservice.persistance.model.AccessToken;
import cz.esc.iot.cloudservice.persistance.model.RefreshToken;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import cz.esc.iot.cloudservice.support.ErrorJson;

/**
 * New UserEntity object is stored into database.
 */
public class UserRegistrator extends ServerResource {
	
	@Post("json")
	public String newUser(Representation entity) throws IOException, OAuthException, JSONException {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
			String json = entity.getText();
			
			AccessTokenRequest request = gson.fromJson(json, AccessTokenRequest.class);
			
			// control request arguments
			if (request.getGrant_type() == null || request.getClient_id() == null || request.getCode() == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return gson.toJson(new ErrorJson("invalid_request"));
			} else if (!request.getGrant_type().equals("authorization_code")) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return gson.toJson(new ErrorJson("unsupported_grant_type"));
			} else if (!request.getClient_id().equals("android") && !request.getClient_id().equals("javascript")) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return gson.toJson(new ErrorJson("invalid_client"));
			}
			
			boolean redirect = (request.getClient_id().equals("android")) ? false : true;
			
			// exchange authorisation code for info about user from Google
			GoogleUserInfo googleUser;
			try {
				googleUser = OAuth2.getGoogleUserInfoFromCode(request.getCode(), redirect);
			} catch (OAuthException e) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return gson.toJson(new ErrorJson("invalid_grant"));
			}
	
			// register user
			UserEntity newUser = gson.fromJson(json, UserEntity.class);
			if (newUser.getPassword() == null) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return gson.toJson(new ErrorJson("invalid_password"));
			}
			newUser.setEmail(googleUser.getEmail());
			MorfiaSetUp.getDatastore().save(newUser);
			
			// generate and return token
			CloudToken token = OAuth2.generateToken();
			
			AccessToken accessToken = new AccessToken(token.getAccess_token(), newUser);
			RefreshToken refreshToken = new RefreshToken(token.getRefresh_token(), newUser);
			MorfiaSetUp.getDatastore().save(accessToken);
			MorfiaSetUp.getDatastore().save(refreshToken);
			
			return gson.toJson(token);
		}
		getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		return gson.toJson(new ErrorJson("invalid_request"));
	}
}
