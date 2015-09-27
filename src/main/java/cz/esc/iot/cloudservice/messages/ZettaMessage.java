package cz.esc.iot.cloudservice.messages;

/**
 * Class for messages which are send through websocket from zetta.
 */
public class ZettaMessage {

	private String topic;
	private long timestamp;
	private String data;

	public String getTopic() {
		return topic;
	}

	public long getTimestamp() {
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