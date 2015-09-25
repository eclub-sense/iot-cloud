package cz.esc.iot.cloudservice.support;

import com.google.gson.annotations.Expose;

public class Parameter {

	@Expose private String name;
	@Expose private String value;
	@Expose private String type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
