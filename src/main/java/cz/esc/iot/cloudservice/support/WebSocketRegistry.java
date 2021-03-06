package cz.esc.iot.cloudservice.support;

import java.util.ArrayList;

import cz.esc.iot.cloudservice.WebSocket;

/**
 * Registry used for storing open zettor<->hub websockets. Variable cloudSocket
 * contains WebSocket instance associated with zetta-cloud.
 */
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

	public static void remove(String uuid) {
		WebSocket s = WebSocketRegistry.get(uuid);
		WebSocketRegistry.remove(s);
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
