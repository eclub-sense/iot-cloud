package cz.esc.iot.cloudservice.unused.persistance.dao.imp;

import cz.esc.iot.cloudservice.persistance.model.MeasureValue;
import cz.esc.iot.cloudservice.persistance.model.SensorTypeInfo;
import cz.esc.iot.cloudservice.unused.CloudMongoDB;
import cz.esc.iot.cloudservice.unused.Dao;
import cz.esc.iot.cloudservice.unused.sensors.SensorType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class SensorTypeInfoDaoImpl implements Dao<SensorTypeInfo> {

    @Override
    public int create(SensorTypeInfo sensor) {
		MongoCollection<Document> collection = CloudMongoDB.getInstance().getCollection("Drivers");
		List<Document> values = new LinkedList<>();
		for (MeasureValue sen : sensor.getValues()) {
			Map<String, Object> map = new HashMap<>();
			map.put("name", sen.getName());
			map.put("unit", sen.getUnit());
			Document document = new Document(map);
			values.add(document);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("type", sensor.getType());
		map.put("type_name", sensor.getTypeName());
		map.put("driver_name", sensor.getDriverName());
		map.put("values", values);
		Document doc = new Document(map);
		collection.insertOne(doc);
		return 0;
    }

    @Override
    public SensorTypeInfo read(SensorType id) {

        SensorTypeInfo zettaDriver = new SensorTypeInfo();
        zettaDriver.setType(id.getCode());
        zettaDriver.setTypeName(id.getName());
        
    	MongoCollection<Document> collection = CloudMongoDB.getInstance().getCollection("Drivers");

		// finds in database
		FindIterable<Document> users = collection.find(new Document("type", id.getCode()));
		SensorTypeInfo sensorInfo = SensorTypeInfoInstanceCreator.createInstance(users.first());
		//System.out.println(sensorInfo);

        return sensorInfo;
    }

    @Override
    public boolean update(SensorTypeInfo zettaDriver) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delete(SensorTypeInfo zettaDriver) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
