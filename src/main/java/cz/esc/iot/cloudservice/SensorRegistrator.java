package cz.esc.iot.cloudservice;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import cz.esc.iot.cloudservice.sensors.Sensor;
import cz.esc.iot.cloudservice.sensors.SensorType;
import cz.esc.iot.cloudservice.sensors.VirtualSensorCreator;

public class SensorRegistrator extends ServerResource {

	@Post
	public void acceptJsonRepresentation(JsonRepresentation entity) throws ResourceException, IOException {
	    if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
	    	try {
	    		JSONObject json = entity.getJsonObject();
				Sensor sensor = VirtualSensorCreator.createSensorInstance(json.getInt("uuid"), SensorType.values()[json.getInt("type")-0x41], json.getInt("secret"));
				System.out.println(sensor);
				System.out.println(sensor.getClass());
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    }
	}
	
	@Get("html")
	public String hello() {
		return "<h1>Hello</h1>";
	}
}
