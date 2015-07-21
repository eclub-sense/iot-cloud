package cz.esc.iot.cloudservice.messages;

import com.google.gson.annotations.Expose;

/**
 * Class for DATA type messages from hub to cloud. Used when sensor sends
 * actualised data.
 */
public class HubDataMsg extends HubMessage {
	
	@Expose private String data;
	
	public String getData() {
		return data;
	}

	@Override
	public String toString() {
		return "HubDataMsg [data=" + data + ", type=" + type + ", uuid=" + uuid + "]";
	}
}
