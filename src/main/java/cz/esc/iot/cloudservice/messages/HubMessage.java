package cz.esc.iot.cloudservice.messages;

public class HubMessage {

	private HubMessageType type;
	private int uuid;
	private byte[] encrypted;
	
	public HubMessage() {
		super();
	}
	
	public HubMessageType getType() {
		return type;
	}
	public void setType(HubMessageType type) {
		this.type = type;
	}
	public int getUuid() {
		return uuid;
	}
	public void setUuid(int uuid) {
		this.uuid = uuid;
	}
	public byte[] getEncrypted() {
		return encrypted;
	}
	public void setEncrypted(byte[] encrypted) {
		this.encrypted = encrypted;
	}	
}
