package cz.esc.iot.cloudservice.registry;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cz.esc.iot.cloudservice.sensors.Sensor;

public class ConnectedSensorList {

	private static ConnectedSensorList sensorlist = null;
	@Expose @SerializedName("@type") private String jsonType = "collection";
	@Expose private int total_count = 0;
	@Expose private ArrayList<Sensor> items = new ArrayList<Sensor>();
	
	public static ConnectedSensorList getInstance() {
		if(sensorlist == null) {
			sensorlist = new ConnectedSensorList();
		}
		return sensorlist;
	}
	
	public ArrayList<Sensor> getList() {
		return items;
	}
	
	public Sensor getSensor(int uuid) {
		for (Sensor sensor : items) {
			if (sensor.getUuid() == uuid) return sensor;
		}
		return null;
	}
	
	public void add (Sensor s) {
		total_count++;
		items.add(s);
	}
	
	public void remove (Sensor s) {
		total_count--;
		items.remove(s);
	}
}
