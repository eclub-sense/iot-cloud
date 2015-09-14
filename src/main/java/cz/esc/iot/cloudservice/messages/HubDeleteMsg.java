package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

public class HubDeleteMsg extends HubMessage {

	@Expose private final String type = "DELETE";

	public HubDeleteMsg(String uuid) {
		this.uuid = uuid;
	}
	
	@Override
	public String getType() {
		return "DELETE";
	}
}
