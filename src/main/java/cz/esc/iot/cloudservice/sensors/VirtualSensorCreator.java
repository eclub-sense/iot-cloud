package cz.esc.iot.cloudservice.sensors;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class VirtualSensorCreator {

	public static Sensor createSensorInstance(String json) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		JSONObject jsonObject;
		Object type = null;
		try {
			jsonObject = new JSONObject(json);
			type = jsonObject.get("type");
			System.out.println(type +""+ type.getClass());
			} catch (JSONException e) {
			e.printStackTrace();
		}
		switch ((String)type) {
		case "THERMOMETER" : return gson.fromJson(json, ESCThermometer.class);
		default : return null;
		}
	}

}
