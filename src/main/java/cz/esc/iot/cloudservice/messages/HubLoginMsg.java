package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

/**
 * Class for LOGIN type messages used for verification of new hub
 * and its websocket connection.
 */
public class HubLoginMsg extends HubMessage{

	@Expose private static final String type = "LOGIN";
	@Expose private String identifier;
	@Expose private String access_token;
	
	@Override
	public String getType() {
		return type;
	}
	public String getIdentifier() {
		return identifier;
	}
	public String getAccess_token() {
		return access_token;
	}
	

}
