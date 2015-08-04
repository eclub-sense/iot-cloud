package cz.esc.iot.cloudservice.persistance.model;

import org.mongodb.morphia.annotations.Embedded;

import com.google.gson.annotations.Expose;

@Embedded
public class Data {
	
	@Expose private String name;
	@Expose private String value;
	
	public Data() {
		
	}
	public Data(String name, String value) {
		super();
		this.name = name;
		this.value = value;
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
		return "Data [name=" + name + ", value=" + value + "]";
	}
	
}
