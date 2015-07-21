package cz.esc.iot.cloudservice.sensors;

public interface WriteableSensor {

	/**
	 * Creates packet to be send to physical sensor.
	 */
	public byte[] writePacket(byte[] payload);
	
	/**
	 * Creates payload from given data.
	 */
	public byte[] createPayload();
	
	/**
	 * @param data Data to be encrypted.
	 * @return Encrypted data.
	 */
	public byte[] encrypt(String data);
}
