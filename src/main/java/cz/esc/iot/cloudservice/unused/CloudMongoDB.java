package cz.esc.iot.cloudservice.unused;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class CloudMongoDB {
	
	private static MongoDatabase db = new MongoClient( "localhost" , 27017 ).getDatabase("CloudDB");
	
	public static MongoDatabase getInstance() {
		return db;
	}
}
