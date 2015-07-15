package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.registry.ConnectedSensorList;
import cz.esc.iot.cloudservice.sensors.ESCThermometer;
import cz.esc.iot.cloudservice.sensors.Sensor;
import cz.esc.iot.cloudservice.sensors.SensorType;
import cz.esc.iot.cloudservice.sensors.VirtualSensorCreator;

public class SensorRegistrator extends ServerResource {

	@Post
	public void acceptRepresentation(Representation entity) throws ResourceException, IOException {
	    if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
    		String json = entity.getText();
    		Sensor sensor = VirtualSensorCreator.createSensorInstance(json);
    		ConnectedSensorList.getInstance().add(sensor);
			System.out.println(sensor);
			//WebSocket.getInstance(0).getRemote().sendString(sensor.getUuid() + ": register");
			System.out.println(ConnectedSensorList.getInstance().getList());
	    }
	}
	
	@Get("html")
	public String hello() {
		return "<h1>Hello</h1>";
	}
}
