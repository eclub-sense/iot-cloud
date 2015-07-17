package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

public class HubLoginMsg extends HubMessage{

	@Expose private String username;
	@Expose private String password;
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "HubLoginMsg [username=" + username + ", password=" + password + ", type=" + type + ", uuid=" + uuid
				+ "]";
	}
}
