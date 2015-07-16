package cz.esc.iot.cloudservice.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class Sensor {
	
	@Expose @SerializedName("@type") private String jsonType = "sensor";
	@Expose protected int uuid;
	@Expose protected SensorType type = SensorType.THERMOMETER;
	@Expose (serialize = false) protected String secret;
	private int incr;
	private int battery;
	private int reserved;
	
	public Sensor() {
		super();
	}
	
	public Sensor(int uuid, SensorType type, String secret) {
		this.uuid = uuid;
		this.type = type;
		this.secret = secret;
	}

	public abstract void setPayload(String data);
	
	public void setMessageParts(String p) {
		System.out.println(p+ " "+ secret);
		String packet = decrypt(p);
		System.out.println("DECR: "+packet);
		System.out.println(packet.substring(0, 1));
		System.out.println(packet.substring(4,5));
		System.out.println(packet.substring(6,11));
		System.out.println(packet.substring(12,packet.length()));
		incr = (int)(Integer.parseInt(packet.substring(0, 1)));
		battery = (int)(Integer.parseInt(packet.substring(4, 5)));
		reserved = (int)(Integer.parseInt(packet.substring(6, 11)));
		setPayload(packet.substring(12, packet.length()));
	}

	private String decrypt(String encrypted) {
		int len = encrypted.length();
		byte[] secretBytes = secret.getBytes();
		byte[] encryptedBytes = encrypted.getBytes();
		String res = "";
		for (int i = 0; i < len; i++) {
			res = res + Byte.toString((byte)(0xff & ((int)secretBytes[i] ^ (int)encryptedBytes[i])));
		}
		return res;
	}
	
	public int getUuid() {
		return uuid;
	}

	public SensorType getType() {
		return type;
	}

	public String getSecret() {
		return secret;
	}

	public String getJsonType() {
		return jsonType;
	}

	public void setJsonType(String jsonType) {
		this.jsonType = jsonType;
	}

	public int getIncr() {
		return incr;
	}

	public void setIncr(int incr) {
		this.incr = incr;
	}

	public int getBattery() {
		return battery;
	}

	public void setBattery(int battery) {
		this.battery = battery;
	}

	public int getReserved() {
		return reserved;
	}

	public void setReserved(int reserved) {
		this.reserved = reserved;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public void setType(SensorType type) {
		this.type = type;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Override
	public String toString() {
		return "Sensor [jsonType=" + jsonType + ", uuid=" + uuid + ", type=" + type + ", secret=" + secret + "]";
	}
}
