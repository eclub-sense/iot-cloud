package cz.esc.iot.cloudservice;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cz.esc.iot.cloudservice.registry.ConnectedSensorList;
import cz.esc.iot.cloudservice.sensors.RegistratedSensors;
import cz.esc.iot.cloudservice.sensors.SensorRegistrator;

public class RestletApplication extends Application {

	public ConnectedSensorList registry;
	
    @Override
    public synchronized Restlet createInboundRoot() {
        //System.out.println("Establish");
        registry = new ConnectedSensorList();
        Router router = new Router(getContext());
        router.attach("/sensor_registration", SensorRegistrator.class);
        router.attach("/registrated_sensors", RegistratedSensors.class);
        return router;
    }
}
