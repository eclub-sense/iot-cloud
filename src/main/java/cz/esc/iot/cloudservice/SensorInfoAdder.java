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

		// Mobile
		list2.clear();
		list2.add(new MeasureValue("lattitude", null));
		list2.add(new MeasureValue("longitude", null));
		info2 = new SensorTypeInfo(8, "gps", list2, "gps-driver");
		System.out.println(MorfiaSetUp.getDatastore());
		MorfiaSetUp.getDatastore().save(info2);
	}
}
