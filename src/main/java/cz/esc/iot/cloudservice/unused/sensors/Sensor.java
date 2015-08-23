package cz.esc.iot.cloudservice.unused.sensors;

import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import cz.esc.iot.cloudservice.unused.Hub;
import cz.esc.iot.cloudservice.unused.registry.Identificable;

public abstract class Sensor implements Identificable {
	
	@Expose @SerializedName("@type") private String jsonType = "sensor";
	@Expose protected String uuid;
	@Expose protected SensorType type = SensorType.THERMOMETER;
	@Expose (serialize = false) protected String secret;
	protected int incr;
	@Expose (deserialize = false) protected int battery;
	@Expose (deserialize = false) protected int hubID;
	protected Hub hub;
	protected byte reserved[] = new byte[3];

	public abstract void readPayload(byte[] data);
	
	public void readPacket(String p) {
		byte[] packet = decrypt(p);
		incr = (int)(packet[0]);
		battery = (int)(packet[2]);
		reserved[0] = packet[3];
		reserved[1] = packet[4];
		reserved[2] = packet[5];
		readPayload(Arrays.copyOfRange(packet, 6, (p.length()/2)+1));
	}
	
	private byte[] decrypt(String encrypted) {
		int len = encrypted.length()/2;
		byte[] secretBytes = DatatypeConverter.parseHexBinary(secret);
		byte[] encryptedBytes = DatatypeConverter.parseHexBinary(encrypted);
		for (int i = 0; i < len; i++) {
			encryptedBytes[i] = (byte)(0xff & ((int)secretBytes[i] ^ (int)encryptedBytes[i]));
		}
		return encryptedBytes;
	}
	
	@Override
	public String getStringUuid() {
		return uuid;
	}

	@Override
	public int getIntUuid() {
		return Integer.parseInt(uuid);
	}
	
	public SensorType getType() {
		return type;
	}

	public String getSecret() {
		return secret;
	}

	public int getIncr() {
		return incr;
	}

	public int getBattery() {
		return battery;
	}

	public Hub getHub() {
		return hub;
	}

	public void setHub(Hub hub) {
		this.hubID = hub.getIntUuid();
		this.hub = hub;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Sensor [jsonType=" + jsonType + ", uuid=" + uuid + ", type=" + type + ", secret=" + secret + "]";
	}
}
