package cz.esc.iot.cloudservice.hubs;

import java.io.IOException;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;
import cz.esc.iot.cloudservice.registry.Identificable;
import cz.esc.iot.cloudservice.sensors.Sensor;

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
	public int getUuid() {
		return uuid;
	}
	
	public void reregisterAllSensors() throws IOException {
		for (Sensor sensor : ConnectedSensorRegistry.getInstance().getList()) {
			if (sensor.getHub().getUuid() == this.uuid)
				registerSensor(sensor.getUuid());
		}
	}
	
	public void registerSensor(int uuid) throws IOException {
		socket.getRemote().sendString("{\"type\":\"NEW\",\"uuid\":" + uuid + "}");
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
