package cz.esc.iot.cloudservice.registry;

import java.util.ArrayList;

import cz.esc.iot.cloudservice.WebSocket;

public class WebSocketRegistry {

	private static ArrayList<WebSocket> sockets = new ArrayList<>();
	private static WebSocket cloudSocket = null;
	
	public static void add(WebSocket s) {
		sockets.add(s);
	}
	
	public static int size() {
		return sockets.size();
	}
	
	public static WebSocket getCloudSocket() {
		return cloudSocket;
	}

	public static void setCloudSocket(WebSocket cloudSocket) {
		WebSocketRegistry.cloudSocket = cloudSocket;
	}

	public static WebSocket get(String uuid) {
		for (WebSocket socket : sockets) {
			if (socket.getHubUuid().equals(uuid)) {
				return socket;
			}
		}
		return null;
	}
	
	public static WebSocket get(int i) {
		return sockets.get(i);
	}
	
	public static void remove(WebSocket s) {
		sockets.remove(s);
	}
}
