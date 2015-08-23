package cz.esc.iot.cloudservice.unused.registry;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectedDeviceList<T extends Identificable> {

	@Expose @SerializedName("@type") protected String jsonType = "collection";
	@Expose protected int total_count = 0;
	@Expose protected ArrayList<T> items = new ArrayList<T>();
	
	public ArrayList<T> getList() {
		return items;
	}
	
	public T get(int uuid) {
		for (T device : items) {
			if (device.getIntUuid() == uuid) return device;
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

	public int getTotalCount() {
		return total_count;
	}

	public void setTotalCount(int total_count) {
		this.total_count = total_count;
	}
}
