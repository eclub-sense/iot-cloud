package cz.esc.iot.cloudservice.registry;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectedDeviceList<T extends Identificable> {

	@Expose @SerializedName("@type") private String jsonType = "collection";
	@Expose private int total_count = 0;
	@Expose protected ArrayList<T> items = new ArrayList<T>();
	
	public ArrayList<T> getList() {
		return items;
	}
	
	public T get(int uuid) {
		for (T device : items) {
			if (device.getUuid() == uuid) return device;
		}
		return null;
	}
	
	public void add (T s) {
		total_count++;
		items.add(s);
	}
	
	public void remove (T s) {
		total_count--;
		items.remove(s);
	}
}
