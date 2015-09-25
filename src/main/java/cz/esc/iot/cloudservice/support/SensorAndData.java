package cz.esc.iot.cloudservice.support;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.Expose;

import cz.esc.iot.cloudservice.persistance.model.SensorEntity;

/**
 * When user asks /registered_sensors/{uuid}, object of this class is created, filled
 * with sensor's info and sensor's data and then serialised by Gson and returned to user.
 */
public class SensorAndData {

	@Expose private String origin;
	@Expose private String permission;
	@Expose private SensorEntity sensor;
	@Expose private List<Action> actions = new LinkedList<>();
	@Expose private List<DataList> measured = new LinkedList<>();
	
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public SensorEntity getSensor() {
		return sensor;
	}
	public void setSensor(SensorEntity sensor) {
		this.sensor = sensor;
	}
	public List<DataList> getMeasured() {
		return measured;
	}
	public void setMeasured(List<DataList> measured) {
		this.measured = measured;
	}
	public void addDataList(DataList list) {
		measured.add(list);
	}
	public List<Action> getActions() {
		return actions;
	}
	public void setActions(List<Action> actions) {
		this.actions = actions;
	}
}
