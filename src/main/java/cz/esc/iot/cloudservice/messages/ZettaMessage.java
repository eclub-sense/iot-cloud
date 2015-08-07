package cz.esc.iot.cloudservice.messages;

public class ZettaMessage {

	private String topic;
	private String timestamp;
	private String data;

	public String getTopic() {
		return topic;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getData() {
		return data;
	}

	@Override
	public String toString() {
		return "ZettaMessage [topic=" + topic + ", timestamp=" + timestamp + ", data=" + data + "]";
	}
	
}