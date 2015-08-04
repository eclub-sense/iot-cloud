package cz.esc.iot.cloudservice.sensors;

/**
 * Types of sensors.
 */
public enum SensorType {
	A_THERMOMETER(1, "a_thermometer"),
	A_ACCELEROMETER(2, "a_accelerometer"),
	A_LEDS(3, "a_leds"),
	A_FAN(4, "a_fan"),
	
	THERMOMETER(0x41, "thermometer"), // 0x41
	LED(0x42, "led");

	private final int code;
	private final String name;

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	private SensorType(int code, String name) {
		this.code = code;
		this.name = name;
	}
}
