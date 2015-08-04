package cz.esc.iot.cloudservice.messages;

import java.util.List;

import com.google.gson.annotations.Expose;

import cz.esc.iot.cloudservice.persistance.model.Data;

/**
 * Class for DATA type messages from hub to cloud. Used when sensor sends
 * actualised data.
 */
public class HubDataMsg extends HubMessage {
	
	@Expose private List<Data> data;
	
	public List<Data> getData() {
		return data;
	}

	@Override
	public String toString() {
		return "HubDataMsg [data=" + data + ", type=" + type + ", uuid=" + uuid + "]";
	}
}
