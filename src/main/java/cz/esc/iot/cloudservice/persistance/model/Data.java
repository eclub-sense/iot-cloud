package cz.esc.iot.cloudservice.persistance.model;

import java.util.Date;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Indexed;

import com.google.gson.annotations.Expose;

@Embedded
public class Data {
	
	@Expose private String name;
	@Expose private String value;
	@Indexed(expireAfterSeconds = 5)
	@Expose private Date time;
	
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
