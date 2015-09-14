package cz.esc.iot.cloudservice.support;

import java.util.List;

import com.google.gson.annotations.Expose;

import cz.esc.iot.cloudservice.persistance.model.Data;

public class DataList {

	@Expose private String name;
	@Expose private String websocket;
	@Expose private List<Data> items;
	
	public DataList() {
		super();
	}
	public DataList(String value, List<Data> list, String ws) {
		name = value;
		items = list;
		websocket = ws;
	}
	public String getWebsocket() {
		return websocket;
	}
	public String getName() {
		return name;
	}
	public List<Data> getItems() {
		return items;
	}
}
