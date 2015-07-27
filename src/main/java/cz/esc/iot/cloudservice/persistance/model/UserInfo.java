package cz.esc.iot.cloudservice.persistance.model;

import java.util.List;

public class UserInfo {

	private String username;
	private int port;
	private List<Integer> uuids;
	
	public UserInfo(String u, int p, List<Integer> l) {
		username = u;
		port = p;
		uuids = l;
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getPort() {
		return port;
	}
	
	public List<Integer> getUuids() {
		return uuids;
	}

	@Override
	public String toString() {
		return "UserInfo [username=" + username + ", port=" + port + ", uuids=" + uuids + "]";
	}
}
