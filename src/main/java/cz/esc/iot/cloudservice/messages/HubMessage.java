package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

/**
 * Class for messages from hub. Parent class for various messages types.
 */
public abstract class HubMessage {

	@Expose protected String uuid;
	
	public HubMessage() {
		super();
	}
	
	public abstract String getType();
	
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
