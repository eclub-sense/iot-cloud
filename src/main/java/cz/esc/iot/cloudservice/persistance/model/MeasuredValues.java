package cz.esc.iot.cloudservice.persistance.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.Expose;

@Entity
public class MeasuredValues {

	@Id
	private ObjectId id;
	@Reference
	@Expose private SensorEntity sensor;
	@Embedded
	@Expose private List<Data> data;
	
	public MeasuredValues() {
		super();
	}
	public MeasuredValues(ObjectId id, SensorEntity sensor, List<Data> data) {
		super();
		this.id = id;
		this.sensor = sensor;
		this.data = data;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public SensorEntity getSensor() {
		return sensor;
	}
	public void setSensor(SensorEntity sensor) {
		this.sensor = sensor;
	}
	public List<Data> getData() {
		return data;
	}
	public void setData(List<Data> data) {
		this.data = data;
	}
	
}
