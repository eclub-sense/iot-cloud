package cz.esc.iot.cloudservice.support;

import java.util.List;

import com.google.gson.annotations.Expose;

import cz.esc.iot.cloudservice.persistance.model.Data;

public class DataList {

	@Expose private String name;
	@Expose private List<Data> items;
	
	public DataList() {
		super();
	}
	public DataList(String value, List<Data> list) {
		name = value;
		items = list;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Data> getItems() {
		return items;
	}
	public void setItems(List<Data> itemes) {
		this.items = itemes;
	}
}
