package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

public class HubDataMsg extends HubMessage {
	
	@Expose private String data;
	
	public String getData() {
		return data;
	}
	
	public void setData(String encrypted) {
		this.data = encrypted;
	}

	@Override
	public String toString() {
		return "HubDataMsg [data=" + data + ", type=" + type + ", uuid=" + uuid + "]";
	}
}
