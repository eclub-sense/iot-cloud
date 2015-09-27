package cz.esc.iot.cloudservice.persistance.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Reference;

import com.google.gson.annotations.Expose;

/**
 * Morfia's entity representing one measured value.
 */
@Entity
@Indexes(@Index(name = "name, -time", unique = true))
public class Data {
	
    @Id
    private ObjectId id;
	private String name;
	@Expose private String value;
	@Indexed(expireAfterSeconds = 24*3600) // data are stored for one day
	@Expose private Date time;
	@Reference
	private SensorEntity sensor;
	
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
