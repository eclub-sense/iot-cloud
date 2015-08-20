package cz.esc.iot.cloudservice.persistance.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.Expose;

@Entity
public class Data {
	
    @Id
    private ObjectId id;
	@Expose private String name;
	@Expose private String value;
	@Indexed(expireAfterSeconds = 24*3600) // one day
	@Expose private Date time;
	@Reference
	@Expose private SensorEntity sensor;
	
	public Data() {
		super();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}
	
	public SensorEntity getSensor() {
		return sensor;
	}

	public void setSensor(SensorEntity sensor) {
		this.sensor = sensor;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Data [name=" + name + ", value=" + value + ", time=" + time + "]";
	}
}
