package cz.esc.iot.cloudservice;

import java.util.LinkedList;
import java.util.List;

import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.MeasureValue;
import cz.esc.iot.cloudservice.persistance.model.SensorTypeInfo;

public class SensorInfoAdder {

	public static void main(String[] args) {
		List<MeasureValue> list1 = new LinkedList<>();
		list1.add(new MeasureValue("intensity", "c"));
		SensorTypeInfo info1 = new SensorTypeInfo(5, "zetta-photocell", list1, "zetta-photocell-mock-driver");
		List<MeasureValue> list2 = new LinkedList<>();
		list2.add(new MeasureValue("state", null));
		SensorTypeInfo info2 = new SensorTypeInfo(6, "zetta-led", list2, "zetta-led-mock-driver");

		MorfiaSetUp.getDatastore().save(info1);
		MorfiaSetUp.getDatastore().save(info2);
	}
}
