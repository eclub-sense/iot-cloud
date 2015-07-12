package cz.esc.iot.cloudservice.registry;

import java.util.ArrayList;

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
}
