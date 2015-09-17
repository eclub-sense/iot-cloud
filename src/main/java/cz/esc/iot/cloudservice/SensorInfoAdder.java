package cz.esc.iot.cloudservice;

import java.util.LinkedList;
import java.util.List;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.MeasureValue;
import cz.esc.iot.cloudservice.persistance.model.SensorTypeInfo;

/**
 * Java application for filling MongoDB's SensorTypeInfo collection.
 */
public class SensorInfoAdder {

	public static void main(String[] args) {

		// A_LEDS
		List<MeasureValue> list2 = new LinkedList<>();
		list2.add(new MeasureValue("temperature", "°C"));
		SensorTypeInfo info2 = new SensorTypeInfo(1, "a_thermometer", list2, "a_thermometer_driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		//A_ACCELEROMETER
		list2.clear();
		list2.add(new MeasureValue("x", null));
		list2.add(new MeasureValue("y", null));
		list2.add(new MeasureValue("z", null));
		info2 = new SensorTypeInfo(2, "a_accelerometer", list2, "a_accelerometer_driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		//A_THERMOMETER
		list2.clear();
		list2.add(new MeasureValue("R", "intensity"));
		list2.add(new MeasureValue("G", "intensity"));
		list2.add(new MeasureValue("Y", "intensity"));
		info2 = new SensorTypeInfo(3, "a_leds", list2, "a_leds_driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		//ZETTA_PHOTOCELL
		list2.clear();
		list2.add(new MeasureValue("intensity", null));
		info2 = new SensorTypeInfo(5, "zetta_photocell", list2, "zetta-photocell-mock-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		//ZETTA_LED
		list2.clear();
		list2.add(new MeasureValue("state", null));
		info2 = new SensorTypeInfo(6, "zetta_led", list2, "zetta-led-mock-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);

		// gps
		list2.clear();
		list2.add(new MeasureValue("latitude", null));
		list2.add(new MeasureValue("longitude", null));
		info2 = new SensorTypeInfo(8, "gps", list2, "gps-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// accel
		list2.clear();
		list2.add(new MeasureValue("x", null));
		list2.add(new MeasureValue("y", null));
		list2.add(new MeasureValue("z", null));
		info2 = new SensorTypeInfo(7, "accelerometer", list2, "accelerometer-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// light sensor
		list2.clear();
		list2.add(new MeasureValue("illumination", "lx"));
		info2 = new SensorTypeInfo(9, "light_sensor", list2, "light_sensor-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// proximity sensor
		list2.clear();
		list2.add(new MeasureValue("proximity", "cm"));
		info2 = new SensorTypeInfo(10, "proximity_sensor", list2, "proximity_sensor-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// magnetometer
		list2.clear();
		list2.add(new MeasureValue("x", null));
		list2.add(new MeasureValue("y", null));
		list2.add(new MeasureValue("z", null));
		info2 = new SensorTypeInfo(11, "magnetometer", list2, "magnetometer-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// gyroscope
		list2.clear();
		list2.add(new MeasureValue("x", null));
		list2.add(new MeasureValue("y", null));
		list2.add(new MeasureValue("z", null));
		info2 = new SensorTypeInfo(12, "gyroscope", list2, "gyroscope-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// barometer
		list2.clear();
		list2.add(new MeasureValue("pressure", "hPa"));
		info2 = new SensorTypeInfo(13, "barometer", list2, "barometer-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// gravity
		list2.clear();
		list2.add(new MeasureValue("x", null));
		list2.add(new MeasureValue("y", null));
		list2.add(new MeasureValue("z", null));
		info2 = new SensorTypeInfo(14, "gravity_sensor", list2, "gravity_sensor-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// linear accel
		list2.clear();
		list2.add(new MeasureValue("x", null));
		list2.add(new MeasureValue("y", null));
		list2.add(new MeasureValue("z", null));
		info2 = new SensorTypeInfo(15, "linear_accelerometer", list2, "linear_accelerometer-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// rotation_sensor
		list2.clear();
		list2.add(new MeasureValue("x", null));
		list2.add(new MeasureValue("y", null));
		list2.add(new MeasureValue("z", null));
		info2 = new SensorTypeInfo(16, "rotation_sensor", list2, "rotation_sensor-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// humidity_sensor
		list2.clear();
		list2.add(new MeasureValue("humidity", "%"));
		info2 = new SensorTypeInfo(17, "humidity_sensor", list2, "humidity_sensor-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// ambient_thermometer
		list2.clear();
		list2.add(new MeasureValue("temperature", "°C"));
		info2 = new SensorTypeInfo(18, "ambient_thermometer", list2, "ambient_thermometer-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// _thermometer
		list2.clear();
		list2.add(new MeasureValue("temperature", "°C"));
		list2.add(new MeasureValue("humudity", "%"));
		list2.add(new MeasureValue("pressure", "Pa"));
		list2.add(new MeasureValue("increment", null));
		list2.add(new MeasureValue("vbat", "mV"));
		list2.add(new MeasureValue("rssi", "dBm"));
		info2 = new SensorTypeInfo(128, "esc_thermometer", list2, "esc_thermometer-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// esc_accel
		list2.clear();
		list2.add(new MeasureValue("accX", null));
		list2.add(new MeasureValue("accY", null));
		list2.add(new MeasureValue("accZ", null));
		list2.add(new MeasureValue("increment", null));
		list2.add(new MeasureValue("vbat", "mV"));
		info2 = new SensorTypeInfo(89, "esc_accelerometer", list2, "esc_accelerometer-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// esc_pir
		list2.clear();
		list2.add(new MeasureValue("pir", null));
		list2.add(new MeasureValue("vbat", "mV"));
		list2.add(new MeasureValue("rssi", "dBm"));
		info2 = new SensorTypeInfo(129, "esc_pir", list2, "esc_pir-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
		
		// esc_lcd
		list2.clear();		
		list2.add(new MeasureValue("state", null));
		info2 = new SensorTypeInfo(123, "esc_lcd", list2, "esc_lcd-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
	}
}
