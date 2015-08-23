package cz.esc.iot.cloudservice.unused.sensors;

import java.util.Arrays;

import com.google.gson.annotations.Expose;

public class ESCLed extends Sensor implements WriteableSensor {

	@Expose (deserialize = false) protected Switch led;
	
	@Override
	public void readPayload(byte[] data) {
		led = (data[0] < 0) ? Switch.OFF : Switch.ON;
		
	}

	@Override
	public byte[] writePacket(byte[] payload) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] createPayload() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] encrypt(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	public Switch getLed() {
		return led;
	}

	@Override
	public String toString() {
		return "ESCLed [led=" + led + ", uuid=" + uuid + ", type=" + type + ", secret=" + secret + ", incr=" + incr
				+ ", battery=" + battery + ", hubID=" + hubID + ", hub=" + hub + ", reserved="
				+ Arrays.toString(reserved) + "]";
	}
}
