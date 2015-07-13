package cz.esc.iot.cloudservice.registry;

import java.util.ArrayList;

import org.restlet.ext.json.JsonRepresentation;

import cz.esc.iot.cloudservice.sensors.Sensor;

public class ConnectedSensorList {

	private ArrayList<Sensor> list;
	
	public ConnectedSensorList() {
		list = new ArrayList<Sensor>();
	}
	
	public ArrayList<Sensor> getList() {
		return list;
	}
	
	public void add (Sensor s) {
		list.add(s);
	}
	
	public JsonRepresentation toJson() {
		String json = "{\n";
		json = json + "\"Name\" : \"list_of_connected_sensors\",\n"; 
		json = json + "\"Count\" : " + list.size();
		json = (list.size() > 0) ? json + ",\n" : json + "\n";
		for (int i = 1; i <= list.size(); i++) {
			json = json + "\"Sensor " + i + "\" : " + list.get(i-1).toString();
			json = (list.size() == i) ? json + "\n" : json + ",\n";
		}
		json = json + "}";
		return new JsonRepresentation(json);
	}
}
