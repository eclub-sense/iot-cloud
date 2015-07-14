package cz.esc.iot.cloudservice;

import java.io.BufferedReader;
import java.io.FileReader;

import cz.esc.iot.cloudservice.resources.RegisteredSensors;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cz.esc.iot.cloudservice.registry.ConnectedSensorList;
import cz.esc.iot.cloudservice.resources.SensorRegistrator;

public class RestletApplication extends Application {

	public ConnectedSensorList registry;
	
    @Override
    public synchronized Restlet createInboundRoot() {
        registry = new ConnectedSensorList();
        
        new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//System.out.println("init");
					BufferedReader pipe = new BufferedReader(new FileReader("pipe.fifo"));
				    String res = pipe.readLine();
				    System.out.println("RESTLET: " + res);
				    pipe.close();
				} catch (Exception e) {
				    e.printStackTrace();
				}
			}
        }).start();
        
        Router router = new Router(getContext());
        router.attach("/sensor_registration", SensorRegistrator.class);
        router.attach("/registered_sensors", RegisteredSensors.class);
        return router;
    }
}
