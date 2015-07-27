package cz.esc.iot.cloudservice.persistance.dao.imp;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

import cz.esc.iot.cloudservice.database.CloudMongoDB;
import cz.esc.iot.cloudservice.persistance.dao.Dao;
import cz.esc.iot.cloudservice.persistance.model.UserInfo;
import cz.esc.iot.cloudservice.sensors.SensorType;

public class UserInfoDaoImpl implements Dao<UserInfo> {

	@Override
	public int create(UserInfo user) {
		MongoCollection<Document> collection = CloudMongoDB.getInstance().getCollection("Users");
		Map<String, Object> map = new HashMap<>();
		map.put("username", user.getUsername());
		map.put("port", user.getPort());
		map.put("uuids", user.getUuids());
		Document doc = new Document(map);
		collection.insertOne(doc);
		return 0;
	}

	@Override
	public UserInfo read(SensorType id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(UserInfo zettaDriver) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(UserInfo zettaDriver) {
		// TODO Auto-generated method stub
		return false;
	}
}
