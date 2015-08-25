package cz.esc.iot.cloudservice;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.ext.oauth.OAuthProxy;
import org.restlet.routing.Router;

import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.resources.Homepage;
import cz.esc.iot.cloudservice.resources.AccessToken;
import cz.esc.iot.cloudservice.resources.JavaScript;
import cz.esc.iot.cloudservice.resources.RegisteredHubs;
import cz.esc.iot.cloudservice.resources.RegisteredSensors;
import cz.esc.iot.cloudservice.resources.SensorRegistrator;
import cz.esc.iot.cloudservice.resources.ShareSensor;

/**
 * Restlet application called when querying whatever url except to /events.
 */
public class RestletApplication extends Application {
	
    @Override
    public synchronized Restlet createInboundRoot() {
    	
    	OAuth2.setCredentials();
    	String scopes[] = {"email"};
    	
    	Router router = new Router(getContext());
    	
    	/*
    	 * When querying /login url, user is redirected to Google
    	 * authentication servers.
    	 */
    	OAuthProxy proxy = new OAuthProxy(getContext(), true);
    	proxy.setClientId(OAuth2.clientID);
    	proxy.setClientSecret(OAuth2.clientSecret);
    	proxy.setRedirectURI("http://mlha-139.sin.cvut.cz:8080/callback");
    	proxy.setAuthorizationURI("https://accounts.google.com/o/oauth2/auth");
    	proxy.setTokenURI("https://accounts.google.com/o/oauth2/token");
    	proxy.setScope(scopes);
    	proxy.setNext(AccessToken.class);
        router.attach("/login", proxy);
        router.attach("/callback", AccessToken.class);
        
        router.attach("/", Homepage.class);
        router.attach("/app.js", JavaScript.class);
        
        /*
         * These resources are accessible with valid access token only.
         */
        router.attach("/sensor_registration", SensorRegistrator.class);
        router.attach("/registered_sensors", RegisteredSensors.class);
        router.attach("/registered_sensors/{uuid}", RegisteredSensors.class);
        router.attach("/registered_hubs/{uuid}", RegisteredHubs.class);
        router.attach("/registered_hubs", RegisteredHubs.class);
        router.attach("/share_sensor", ShareSensor.class);
        
        return router;
    }
}
