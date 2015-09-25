package cz.esc.iot.cloudservice.support;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class Action {

	@Expose private String name;
	@Expose private List<Parameter> fields = new LinkedList<>();
	
	public Action(String name, List<Parameter> params) {
		super();
		this.name = name;
		this.fields = params;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Parameter> getFields() {
		return fields;
	}

	public void setFields(List<Parameter> fields) {
		this.fields = fields;
	}
}
