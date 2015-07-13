package cz.esc.iot.cloudservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.nio.CharBuffer;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import cz.esc.iot.cloudservice.registry.ConnectedSensorList;
import cz.esc.iot.cloudservice.resources.RegistratedSensors;
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
					System.out.println("a");
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
        router.attach("/registrated_sensors", RegistratedSensors.class);
        return router;
    }
}
