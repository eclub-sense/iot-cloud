package cz.esc.iot.cloudservice.hubs;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.registry.Identificable;

public class Hub implements Identificable {

	@Expose @SerializedName("@type") private String jsonType = "hub";
	@Expose protected int uuid;
	protected WebSocket socket;
	@Expose (deserialize = false) protected String status = "connected";
	
	public Hub(int uuid, WebSocket socket) {
		this.uuid = uuid;
		this.socket = socket;
	}
	
	@Override
	public int getIntUuid() {
		return uuid;
	}
	
	public String getStringUuid() {
		return String.format("%08d", uuid);
	}
	
	public WebSocket getSocket() {
		return socket;
	}
	
	public void setSocket(WebSocket socket) {
		status = (socket == null) ? "disconnected" : "connected";
		this.socket =  socket;
	}

	@Override
	public String toString() {
		return "Hub [uuid=" + uuid + ", socket=" + socket + "]";
	}
}
