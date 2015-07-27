package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;

import cz.esc.iot.cloudservice.database.CloudMongoDB;

/**
 * Register new hub.
 */
public class HubRegistrator extends ServerResource {

	public String getLoggedUsername() {
		/*String username = getRequest().getChallengeResponse().getRawValue();
		username = username.split(",")[0];
		username = username.substring(10, username.length()-1);*/
		String username = getRequest().getChallengeResponse().getIdentifier();
		return username;
	}
	
	@Post("json")
	public String registerHub(Representation entity) throws IOException {
		String username = getLoggedUsername();
		String json = entity.getText();
		JSONObject jsonObject;
		int uuid = -1;
		try {
			jsonObject = new JSONObject(json);
			uuid = (int)jsonObject.get("uuid");
			} catch (JSONException e) {
			e.printStackTrace();
		}
		
		int port = updateDatabase(username, uuid);
		
		return "{\"port\":" + port + "}";
	}
	
	private int updateDatabase(String username, int uuid) {
		MongoCollection<Document> collection = CloudMongoDB.getInstance().getCollection("Users");
		
		UpdateResult res = collection.updateOne(new Document("username", username), new Document("$push", new Document("uuids", uuid)));
		FindIterable<Document> users = collection.find(new Document("username", username));
		int port = 0;
		if (res.getMatchedCount() != 0) {
			port = users.first().getInteger("port");
		}
		
		System.out.println(res.getMatchedCount());
		System.out.println(users.first());
		
		return port;
	}
}
