package cz.esc.iot.cloudservice;

import cz.esc.iot.cloudservice.resources.Homepage;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cz.esc.iot.cloudservice.resources.UserRegistrator;
import cz.esc.iot.cloudservice.resources.RegisteredHubs;
import cz.esc.iot.cloudservice.resources.RegisteredSensors;
import cz.esc.iot.cloudservice.resources.SensorRegistrator;

public class RestletApplication extends Application {
	
    @Override
    public synchronized Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/sensor_registration", SensorRegistrator.class);
        router.attach("/registered_sensors/{uuid}", RegisteredSensors.class);
        router.attach("/registered_sensors", RegisteredSensors.class);
        router.attach("/registered_hubs/{uuid}", RegisteredHubs.class);
        router.attach("/registered_hubs", RegisteredHubs.class);
        router.attach("/user_registration", UserRegistrator.class);
        router.attach("/", Homepage.class);
        return router;
    }
}
