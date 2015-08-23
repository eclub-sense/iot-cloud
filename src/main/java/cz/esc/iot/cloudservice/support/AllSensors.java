package cz.esc.iot.cloudservice.support;

import java.util.List;

import com.google.gson.annotations.Expose;

import cz.esc.iot.cloudservice.persistance.model.SensorAccessEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;

/**
 * Instance of this class contains lists of owned, borrowed and public sensors.
 * When user asks /registered_sensors, object of this class is created, filled and
 * then serialised with Gson and returned to user.
 */
public class AllSensors {

	@Expose private List<SensorEntity> _my;
	@Expose private List<SensorAccessEntity> _borrowed;
	@Expose private List<SensorEntity> _public;
	
	public List<SensorEntity> getMy() {
		return _my;
	}
	public void setMy(List<SensorEntity> my) {
		this._my = my;
	}
	public List<SensorAccessEntity> getBorrowed() {
		return _borrowed;
	}
	public void setBorrowed(List<SensorAccessEntity> borrowed) {
		this._borrowed = borrowed;
	}
	public List<SensorEntity> get_public() {
		return _public;
	}
	public void set_public(List<SensorEntity> _public) {
		this._public = _public;
	}
}
