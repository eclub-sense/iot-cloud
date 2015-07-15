package cz.esc.iot.cloudservice.registry;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cz.esc.iot.cloudservice.sensors.Sensor;

public class ConnectedSensorList {

	private static ConnectedSensorList sensorlist = null;
	@Expose @SerializedName("@type") private String jsonType = "collection";
	@Expose private ArrayList<Sensor> items = new ArrayList<Sensor>();
	@Expose private int total_count = items.size();
	
	public static ConnectedSensorList getInstance() {
		if(sensorlist == null) {
			sensorlist = new ConnectedSensorList();
		}
		return sensorlist;
	}
	
	public ArrayList<Sensor> getList() {
		return items;
	}
	
	public void add (Sensor s) {
		items.add(s);
	}
}
