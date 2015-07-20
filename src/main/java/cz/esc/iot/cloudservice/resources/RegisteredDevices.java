package cz.esc.iot.cloudservice.resources;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.registry.ConnectedHubRegistry;
import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;
import cz.esc.iot.cloudservice.sensors.Sensor;
import cz.esc.iot.cloudservice.sensors.SensorType;

public class RegisteredDevices extends ServerResource {
	
	@Get("json")
	public String returnList() {
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String path = this.getRequest().getResourceRef().getPath();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		switch (path) {
		case "/registered_sensors" : return registeredSensors(gson, form);
		case "/registered_hubs" : return gson.toJson(ConnectedHubRegistry.getInstance());
		default : return null;
		}
	}
	
	private String registeredSensors(Gson gson, Form form) {
		ConnectedSensorRegistry result = new ConnectedSensorRegistry();
		SensorType type = null;
		for (Parameter parameter : form) {
			if (parameter.getName().equals("type")) {
				type = SensorType.valueOf(parameter.getValue());
				break;
			}
		}
		if (type == null)
			return gson.toJson(ConnectedSensorRegistry.getInstance());
		else {
			for (Sensor sensor : ConnectedSensorRegistry.getInstance().getList()) {
				if (sensor.getType() == type) {
					result.add(sensor);
				}
			}
			result.setTotalCount(ConnectedSensorRegistry.getInstance().getList().size());
			return gson.toJson(result);
		}
	}
}
