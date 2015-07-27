package cz.esc.iot.cloudservice.persistance.dao.imp;

import org.bson.Document;

import com.google.gson.Gson;

import cz.esc.iot.cloudservice.persistance.model.SensorTypeInfo;

public class SensorTypeInfoInstanceCreator {

	public static SensorTypeInfo createInstance(Document doc) {
		String json = doc.toJson();
		Gson gson = new Gson();
		SensorTypeInfo res = gson.fromJson(json, SensorTypeInfo.class);
		return res;
	}
}
