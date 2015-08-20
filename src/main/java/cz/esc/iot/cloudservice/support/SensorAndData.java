package cz.esc.iot.cloudservice.support;

import java.util.List;

import com.google.gson.annotations.Expose;

import cz.esc.iot.cloudservice.persistance.model.Data;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;

public class SensorAndData {

	@Expose private SensorEntity sensor;
	@Expose private List<Data> measured;
	
	public SensorEntity getSensor() {
		return sensor;
	}
	public void setSensor(SensorEntity sensor) {
		this.sensor = sensor;
	}
	public List<Data> getMeasured() {
		return measured;
	}
	public void setMeasured(List<Data> measured) {
		this.measured = measured;
	}
}
