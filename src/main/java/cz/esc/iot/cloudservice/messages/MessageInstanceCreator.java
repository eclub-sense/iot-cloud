package cz.esc.iot.cloudservice.messages;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Creates instance of subclass of HubMessage according to message type.
 */
public class MessageInstanceCreator {

	public static HubMessage createMsgInstance(String json) throws Exception {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		JSONObject jsonObject;
		Object type = null;
		try {
			jsonObject = new JSONObject(json);
			type = jsonObject.get("type");
			} catch (JSONException e) {
			e.printStackTrace();
		}
		if (type == null) throw new Exception("Bad message format.");
		switch ((String)type) {
		case "DATA" : return gson.fromJson(json, HubDataMsg.class);
		case "LOGIN" : return gson.fromJson(json, HubLoginMsg.class);
		case "DISCOVERED" : return gson.fromJson(json, HubDiscoveredMsg.class);
		default : return null;
		}
	}
}
