package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

public class HubMessage {

	@Expose private HubMessageType type;
	@Expose private String uuid;
	@Expose private String data;
	
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
	public String getEncrypted() {
		return data;
	}
	public void setEncrypted(String encrypted) {
		this.data = encrypted;
	}

	@Override
	public String toString() {
		return "HubMessage [type=" + type + ", uuid=" + uuid + ", data=" + data + "]";
	}	
}
