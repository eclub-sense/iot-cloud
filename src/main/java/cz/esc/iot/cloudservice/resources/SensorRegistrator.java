package cz.esc.iot.cloudservice.resources;

import java.io.IOException;

import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;
import cz.esc.iot.cloudservice.sensors.Sensor;
import cz.esc.iot.cloudservice.sensors.SensorInstanceCreator;

public class SensorRegistrator extends ServerResource {

	@Post
	public void acceptRepresentation(Representation entity) throws ResourceException, IOException {
	    if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON)) {
    		String json = entity.getText();
    		Sensor sensor = SensorInstanceCreator.createSensorInstance(json);
    		ConnectedSensorRegistry.getInstance().add(sensor);
			//WebSocket.getInstance(0).getRemote().sendString("{\"type\":\"NEW\",\"uuid\":" + sensor.getUuid() + "}");
	    }
	}
	
	@Get("html")
	public String hello() {
		return "<h1>Hello</h1>";
	}
}
