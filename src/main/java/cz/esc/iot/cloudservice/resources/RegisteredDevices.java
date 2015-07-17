package cz.esc.iot.cloudservice.resources;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.registry.ConnectedHubRegistry;
import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;

public class RegisteredDevices extends ServerResource {
	
	@Get("json")
	public String returnList() {
		String path = this.getRequest().getResourceRef().getPath();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		switch (path) {
		case "/registered_sensors" : return gson.toJson(ConnectedSensorRegistry.getInstance());
		case "/registered_hubs" : return gson.toJson(ConnectedHubRegistry.getInstance());
		default : return null;
		}
	}
}
