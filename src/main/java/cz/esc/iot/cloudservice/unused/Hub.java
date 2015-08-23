package cz.esc.iot.cloudservice.unused;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.unused.registry.Identificable;

public class Hub implements Identificable {

	@Expose @SerializedName("@type") private String jsonType = "hub";
	@Expose protected String uuid;
	protected WebSocket socket;
	@Expose (deserialize = false) protected String status = "connected";
	
	public Hub(String uuid, WebSocket socket) {
		this.uuid = uuid;
		this.socket = socket;
	}
	
	@Override
	public int getIntUuid() {
		return Integer.parseInt(uuid);
	}
	
	@Override
	public String getStringUuid() {
		return uuid;
	}
	
	public WebSocket getSocket() {
		return socket;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
