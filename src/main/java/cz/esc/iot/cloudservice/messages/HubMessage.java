package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

/**
 * Class for messages from hub. Parent class for various messages types.
 */
public class HubMessage {

	@Expose protected HubMessageType type;
	@Expose protected String uuid;
	
	public HubMessage() {
		super();
	}
	
	public HubMessageType getType() {
		return type;
	}
	
	public void setType(HubMessageType type) {
		this.type = type;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public int getIntUuid() {
		return Integer.parseInt(uuid);
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
