package cz.esc.iot.cloudservice.resources;

import java.io.IOException;
import java.util.Random;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import cz.esc.iot.cloudservice.hubs.Hub;
import cz.esc.iot.cloudservice.messages.Postman;
import cz.esc.iot.cloudservice.registry.ConnectedHubRegistry;
import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;
import cz.esc.iot.cloudservice.sensors.Sensor;
import cz.esc.iot.cloudservice.sensors.SensorInstanceCreator;

public class SensorRegistrator extends ServerResource {

	@Post
	public void acceptRepresentation(Representation entity) throws IOException {
	    if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
    		String json = entity.getText();
    		Sensor sensor = SensorInstanceCreator.createSensorInstance(json);
    		ConnectedSensorRegistry.getInstance().add(sensor);
    		System.out.println(ConnectedHubRegistry.getInstance().getList());
    		
    		Hub hub;
			try {
				hub = chooseHub();
				Postman.registerSensor(hub, sensor.getStringUuid());
				sensor.setHub(hub);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
	}
	
	private Hub chooseHub() throws Exception {
		// register sensor to random hub (dummy choose)
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
