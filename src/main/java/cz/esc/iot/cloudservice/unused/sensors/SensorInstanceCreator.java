package cz.esc.iot.cloudservice.unused.sensors;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Creates instance of subclass of Sensor according to sensor type.
 */
public class SensorInstanceCreator {

	public static Sensor createSensorInstance(String json) {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		JSONObject jsonObject;
		Object type = null;
		try {
			jsonObject = new JSONObject(json);
			type = jsonObject.get("type");
			} catch (JSONException e) {
			e.printStackTrace();
		}
		switch ((String)type) {
		case "THERMOMETER" : return gson.fromJson(json, ESCThermometer.class);
		case "LED" : return gson.fromJson(json, ESCLed.class);
		default : return gson.fromJson(json, ESCThermometer.class);
		}
	}
}
