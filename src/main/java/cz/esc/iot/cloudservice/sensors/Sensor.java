package cz.esc.iot.cloudservice.sensors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class Sensor {
	
	@Expose @SerializedName("@type") private String jsonType = "sensor";
	@Expose protected int uuid;
	@Expose protected SensorType type = SensorType.THERMOMETER;
	@Expose (serialize = false) protected String secret;
	
	public Sensor() {
		super();
	}
	
	public Sensor(int uuid, SensorType type, String secret) {
		this.uuid = uuid;
		this.type = type;
		this.secret = secret;
	}

	public abstract void setBinaryData(byte[] data);

	protected byte[] decrypt(byte[] encrypted, int length) {
		byte[] secretBytes = secret.getBytes();
		byte[] decrypted = new byte[length];
		for (int i = 0; i < length; i++) {
			decrypted[i] = (byte)(0xff & ((int)secretBytes[i] ^ (int)encrypted[i]));
		}
		return decrypted;
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

	@Override
	public String toString() {
		return "SENSOR: {\"uuid\" : " + uuid + ", \"type\" : \""+ type + "\", \"secret\" : " + secret + "}";
	}
}
