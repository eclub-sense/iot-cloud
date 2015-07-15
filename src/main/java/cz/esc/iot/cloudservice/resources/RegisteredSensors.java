package cz.esc.iot.cloudservice.resources;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.registry.ConnectedSensorList;

public class RegisteredSensors extends ServerResource {
	
	@Get("json")
	public String sensors() {
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(ConnectedSensorList.getInstance());
	}
}
