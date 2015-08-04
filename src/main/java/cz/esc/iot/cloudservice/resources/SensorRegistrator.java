package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.util.Random;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.hubs.Hub;
import cz.esc.iot.cloudservice.messages.Postman;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.MeasuredValues;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.registry.ConnectedHubRegistry;
import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;
import cz.esc.iot.cloudservice.sensors.ESCThermometer;
import cz.esc.iot.cloudservice.sensors.Sensor;
import cz.esc.iot.cloudservice.sensors.SensorInstanceCreator;

/**
 * Register new sensor.
 */
public class SensorRegistrator extends ServerResource {

	@Post
	public void acceptRepresentation(Representation entity) throws IOException {
	    if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
    		String json = entity.getText();
    		//Sensor sensor = SensorInstanceCreator.createSensorInstance(json);
    		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    		SensorEntity sensor = gson.fromJson(json, SensorEntity.class);
    		MorfiaSetUp.getDatastore().save(sensor);
    		
    		MeasuredValues values = new MeasuredValues(null, sensor, null);
    		MorfiaSetUp.getDatastore().save(values);
    		
    		//ConnectedSensorRegistry.getInstance().add(sensor);
    		//System.out.println(ConnectedHubRegistry.getInstance().getList());
    		
    		
    		Hub hub;
			try {
				hub = chooseHub();
				System.out.println(sensor);
				System.out.println(sensor.getType());
				Postman.registerSensor(hub, sensor.getUuid(), sensor.getType());
				//sensor.setHub(hub);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	    }
	}
	
	/**
	 * Chooses random hub with which new sensor will be associated.
	 */
	private Hub chooseHub() throws Exception {
		Random randomGenerator = new Random();
		int random = randomGenerator.nextInt(ConnectedHubRegistry.getInstance().getTotalCount());
		int first = random;
		while (ConnectedHubRegistry.getInstance().getList().get(random).getSocket() == null) {
			random = (random+1) % (ConnectedHubRegistry.getInstance().getTotalCount());
			if (random == first) {
				throw new Exception("No hub is connected.");
			}
		}
		return  ConnectedHubRegistry.getInstance().getList().get(random);
	}
	
	@Get("html")
	public String hello() {
		return "<h1>Hello</h1>";
	}
}
