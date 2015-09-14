package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

/**
 * Class for LOGIN type messages used for verification of new hub
 * and its websocket connection.
 */
public class HubLoginMsg extends HubMessage {

	@Expose private final String type = "LOGIN";
	@Expose private String email;
	@Expose private String password;
	
	public String getEmail() {
		return email;
	}
	public String getPassword() {
		return password;
	}
	@Override
	public String getType() {
		return "LOGIN";
	}
}
