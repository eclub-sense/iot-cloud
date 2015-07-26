package cz.esc.iot.cloudservice.resources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import cz.esc.iot.cloudservice.utils.PortNumGenerator;

public class UserRegistrator extends ServerResource {

	@Post("json")
	public String createAccount(Representation entity) throws IOException {
		String json = entity.getText();
		JSONObject jsonObject;
		String password = null;
		String username = null;
		try {
			jsonObject = new JSONObject(json);
			username = (String)jsonObject.get("username");
			password = (String)jsonObject.get("password");
			} catch (JSONException e) {
			e.printStackTrace();
		}
		
		int port = PortNumGenerator.freePort();
		System.out.println(port);
		insertUserToRealm(username, password);
		insertToDatabase(username, port);
		startZettaServer(username, port);
		
		return "{\"status\":\"OK\"}";
	}
	
	private void insertUserToRealm(String username, String password) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/etc/realm.properties", true));
			bw.write(username + ":" + password + ",USER" + "\n");
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startZettaServer(String username, int port) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String[] command = {"/bin/bash", "-c", "bash src/main/bash/start_zetta_server.sh " + username +" "+ port};
				String output;
				try {
					Process p = Runtime.getRuntime().exec(command);
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
					BufferedReader errInput = new BufferedReader(new InputStreamReader(p.getErrorStream()));
					while ((output = stdInput.readLine()) != null) {
					        System.out.println(output);
					}
					while ((output = errInput.readLine()) != null) {
				        System.out.println(output);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void insertToDatabase(String username, int port) {
		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		MongoDatabase db = mongoClient.getDatabase("CloudDB");
		MongoCollection<Document> collection = db.getCollection("Users");
		
		Map<String, Object> map = new HashMap<>();
		map.put("user", username);
		map.put("port", port);
		map.put("uuids", new LinkedList<>());
		Document doc = new Document(map);
		collection.insertOne(doc);
		
		FindIterable<Document> users = collection.find(new Document("user", username));
		System.out.println("Added into database: " + users.first());
		mongoClient.close();
	}
}
