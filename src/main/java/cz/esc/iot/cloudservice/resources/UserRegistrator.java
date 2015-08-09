package cz.esc.iot.cloudservice.resources;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;

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
		
		//int port = PortNumGenerator.freePort();
		insertUserToRealm(username, password);
		insertToDatabase(username, password);
		//startZettaServer(username, port);
		
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
	
	private void insertToDatabase(String username, String password) {
		UserEntity user = new UserEntity();
		user.setUsername(username);
		user.setPassword(password);
		MorfiaSetUp.getDatastore().save(user);
	}
}
