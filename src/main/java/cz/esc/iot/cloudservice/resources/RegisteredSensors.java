package cz.esc.iot.cloudservice.resources;

import java.util.List;

import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.messages.HubDataMsg;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.MeasuredValues;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;
import cz.esc.iot.cloudservice.sensors.Sensor;
import cz.esc.iot.cloudservice.sensors.SensorType;

/**
 * Return list of registered sensors.
 * E.g. with parameters: {SERVER_IP}/registered_sensors?type={SENSOR_TYPE}&hubID={HUB_ID}
 * E.g. for specific sensor according to its uuid: {SERVER_IP}/registered_sensors/{SENSOR_UUID}
 */
public class RegisteredSensors extends ServerResource {
	
	@Get("json")
	public String returnList() {
		//System.out.println(this.getRequest().getChallengeResponse().getIdentifier());
		
		Form form = getRequest().getResourceRef().getQueryAsForm();
		String path = this.getRequest().getResourceRef().getPath();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		switch (path) {
		case "/registered_sensors" : return registeredSensors(gson, form);
		default : return gson.toJson(ConnectedSensorRegistry.getInstance().get(Integer.parseInt((String)this.getRequestAttributes().get("uuid"))));
		}
	}
	
	private String registeredSensors(Gson gson, Form form) {
		//List<SensorEntity> sensors = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).asList();

		List<MeasuredValues> values = MorfiaSetUp.getDatastore().createQuery(MeasuredValues.class).asList();
    	//MorfiaSetUp.getDatastore().update(values, MorfiaSetUp.getDatastore().createUpdateOperations(MeasuredValues.class).addAll("data", ((HubDataMsg)message).getData(), true));
    		
		return gson.toJson(values);
		
		/*
		ConnectedSensorRegistry result = new ConnectedSensorRegistry();
		SensorType type = null;
		int hubID = -1;
		for (Parameter parameter : form) {
			if (parameter.getName().equals("type")) {
				type = SensorType.valueOf(parameter.getValue());
			} else if (parameter.getName().equals("hubID")) {
				hubID = Integer.parseInt(parameter.getValue());
			}
		}
		if (type == null && hubID == -1) {
			return gson.toJson(ConnectedSensorRegistry.getInstance());
		} else if (type == null) {
			for (Sensor sensor : ConnectedSensorRegistry.getInstance().getList()) {
				if (sensor.getHub().getIntUuid() == hubID) {
					result.add(sensor);
				}
			}
			result.setTotalCount(ConnectedSensorRegistry.getInstance().getList().size());
			return gson.toJson(result);
		} else if (hubID == -1) {
			for (Sensor sensor : ConnectedSensorRegistry.getInstance().getList()) {
				if (sensor.getType() == type) {
					result.add(sensor);
				}
			}
			result.setTotalCount(ConnectedSensorRegistry.getInstance().getList().size());
			return gson.toJson(result);
		} else {
			for (Sensor sensor : ConnectedSensorRegistry.getInstance().getList()) {
				if (sensor.getType() == type && sensor.getHub().getIntUuid() == hubID) {
					result.add(sensor);
				}
			}
			result.setTotalCount(ConnectedSensorRegistry.getInstance().getList().size());
			return gson.toJson(result);
		}*/
	}
}
