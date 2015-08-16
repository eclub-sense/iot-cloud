package cz.esc.iot.cloudservice;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.ext.oauth.OAuthProxy;
import org.restlet.routing.Router;

import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.resources.Homepage;
import cz.esc.iot.cloudservice.resources.RegisteredHubs;
import cz.esc.iot.cloudservice.resources.RegisteredSensors;
import cz.esc.iot.cloudservice.resources.SensorRegistrator;
import cz.esc.iot.cloudservice.resources.ShareSensor;

public class RestletApplication extends Application {
	
    @Override
    public synchronized Restlet createInboundRoot() {
    	
    	OAuth2.setCredentials();
    	String scopes[] = {"email"};
    	
    	Router router = new Router(getContext());
    	
    	OAuthProxy root = new OAuthProxy(getContext(), true);
		root.setClientId(OAuth2.clientID);
		root.setClientSecret(OAuth2.clientSecret);
		root.setRedirectURI("http://localhost:8080/callback");
		root.setAuthorizationURI("https://accounts.google.com/o/oauth2/auth");
		root.setTokenURI("https://accounts.google.com/o/oauth2/token");
		root.setScope(scopes);
		root.setNext(Homepage.class);
        router.attach("/", root);
        router.attach("/callback", Homepage.class);
        
        router.attach("/sensor_registration", SensorRegistrator.class);
        router.attach("/registered_sensors/{uuid}", RegisteredSensors.class);
        router.attach("/registered_sensors", RegisteredSensors.class);
        router.attach("/registered_hubs/{uuid}", RegisteredHubs.class);
        router.attach("/registered_hubs", RegisteredHubs.class);
        router.attach("/share_sensor", ShareSensor.class);
        return router;
    }
}
