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
import cz.esc.iot.cloudservice.oauth2.GoogleUserInfo;
import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.oauth2.CloudToken;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.AccessToken;
import cz.esc.iot.cloudservice.persistance.model.RefreshToken;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import cz.esc.iot.cloudservice.support.ErrorJson;

/**
 * Class uses received code and exchange it for valid access token.
 */
public class NewToken extends ServerResource {

	@Post("json")
	public String auth(Representation entity) throws IOException, JSONException {
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
			} else if (!request.getClient_id().equals("dat") && !request.getClient_id().equals("michal")
					&& !request.getClient_id().equals("adam") && !request.getClient_id().equals("login")) {
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return gson.toJson(new ErrorJson("invalid_client"));
			}
			
			// exchange authorisation code for info about user from Google
			GoogleUserInfo googleUser;
			try {
				googleUser = OAuth2.getGoogleUserInfoFromCode(request.getCode(), request.getClient_id());
			} catch (OAuthException e) {
				e.printStackTrace();
				System.out.println("error 4");
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return gson.toJson(new ErrorJson("invalid_grant"));
			}
			
			// find user in db
			UserEntity userEntity = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("email").equal(googleUser.getEmail()).get();
			if (userEntity == null) {
				System.out.println("error 5");
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				return gson.toJson(new ErrorJson("unregistered_user"));
			}
			
			// generate and return token
			CloudToken token = OAuth2.generateToken();

			MorfiaSetUp.getDatastore().delete(MorfiaSetUp.getDatastore().createQuery(AccessToken.class).field("user").equal(userEntity));
			MorfiaSetUp.getDatastore().delete(MorfiaSetUp.getDatastore().createQuery(RefreshToken.class).field("user").equal(userEntity));
			
			AccessToken accessToken = new AccessToken(token.getAccess_token(), userEntity);
			RefreshToken refreshToken = new RefreshToken(token.getRefresh_token(), userEntity);
			MorfiaSetUp.getDatastore().save(accessToken);
			MorfiaSetUp.getDatastore().save(refreshToken);
			
			return gson.toJson(token);
		}
		System.out.println("error 6");
		getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
		return gson.toJson(new ErrorJson("invalid_request"));
	}
}
