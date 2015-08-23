package cz.esc.iot.cloudservice.unused.registry;

import cz.esc.iot.cloudservice.WebSocket;
import cz.esc.iot.cloudservice.unused.Hub;

public class ConnectedHubRegistry extends ConnectedDeviceList<Hub> {

	private static ConnectedHubRegistry list = new ConnectedHubRegistry();
	
	public static ConnectedHubRegistry getInstance() {
		return list;
	}
	
	public Hub whoHasSocket(WebSocket socket) {
		for (Hub device : items) {
			if (device.getSocket() == socket) return device;
		}
		return null;
	}
}
