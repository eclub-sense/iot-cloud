package cz.esc.iot.cloudservice;

import java.util.LinkedList;
import java.util.List;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.MeasureValue;
import cz.esc.iot.cloudservice.persistance.model.SensorTypeInfo;

public class SensorInfoAdder {

	public static void main(String[] args) {
		List<MeasureValue> list2 = new LinkedList<>();
		list2.add(new MeasureValue("R", "intensity"));
		list2.add(new MeasureValue("G", "intensity"));
		list2.add(new MeasureValue("Y", "intensity"));
		SensorTypeInfo info2 = new SensorTypeInfo(3, "a_leds", list2, "a_leds_driver");

		MorfiaSetUp.getDatastore().save(info2);
	}
}
