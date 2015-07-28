package cz.esc.iot.cloudservice.resources;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import cz.esc.iot.cloudservice.database.CloudMongoDB;
import cz.esc.iot.cloudservice.persistance.dao.imp.SensorTypeInfoDaoImpl;
import cz.esc.iot.cloudservice.persistance.dao.imp.UserInfoDaoImpl;
import cz.esc.iot.cloudservice.persistance.model.SensorTypeInfo;
import cz.esc.iot.cloudservice.persistance.model.UserInfo;
import cz.esc.iot.cloudservice.sensors.SensorType;
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
		
		// insert Sensor info into database ------------
		/*
		List<MeasureValue> values = new LinkedList<>();
		values.add(new MeasureValue("temperature", "°C"));
		System.out.println("a");
		values.add(new MeasureValue("pressure", "Bar"));
		SensorTypeInfo sensor = new SensorTypeInfo(0x41, "THERMOMETER", values, "zetta-drivers/0x41/zetta-esc_thermometer-driver.zip", "zetta-esc_thermometer-driver");
		System.out.println(sensor);
		new SensorTypeInfoDaoImpl().create(sensor);
		*/
		
		SensorTypeInfo sen = new SensorTypeInfoDaoImpl().read(SensorType.THERMOMETER);
		System.out.println(sen);

		try {
			ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream("zipfile.zip")));
			//now create the entry in zip file
			 
			ZipEntry entry = new ZipEntry(sen.getDriverName());
			zos.putNextEntry(entry);
			zos.write(sen.getDriver().getData());
			zos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//----------------------------------------------
		
		
		UserInfo user = new UserInfo(username, port, new LinkedList<Integer>());
		new UserInfoDaoImpl().create(user);

		MongoCollection<Document> collection = CloudMongoDB.getInstance().getCollection("Users");
		FindIterable<Document> users = collection.find(new Document("username", username));
		System.out.println("Added into database: " + users.first());
	}
}
