package cz.esc.iot.cloudservice;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cz.esc.iot.cloudservice.registry.ConnectedSensorList;
import cz.esc.iot.cloudservice.resources.RegisteredSensors;
import cz.esc.iot.cloudservice.resources.SensorRegistrator;

public class RestletApplication extends Application {

	public ConnectedSensorList registry = ConnectedSensorList.getInstance();
	static String username = "User";
	static String password = "123";
	
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/sensor_registration", SensorRegistrator.class);
        router.attach("/registered_sensors", RegisteredSensors.class);
        return router;
    }
}
