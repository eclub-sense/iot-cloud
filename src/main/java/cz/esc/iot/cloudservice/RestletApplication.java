package cz.esc.iot.cloudservice;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.ext.oauth.OAuthProxy;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

import cz.esc.iot.cloudservice.oauth2.OAuth2;
import cz.esc.iot.cloudservice.resources.Homepage;
import cz.esc.iot.cloudservice.resources.NewToken;
import cz.esc.iot.cloudservice.resources.API;
import cz.esc.iot.cloudservice.resources.Code;
import cz.esc.iot.cloudservice.resources.Css;
import cz.esc.iot.cloudservice.resources.JavaScript;
import cz.esc.iot.cloudservice.resources.RegisteredHubs;
import cz.esc.iot.cloudservice.resources.RegisteredSensors;
import cz.esc.iot.cloudservice.resources.SensorRegistrator;
import cz.esc.iot.cloudservice.resources.ShareSensor;
import cz.esc.iot.cloudservice.resources.TokenRefresher;
import cz.esc.iot.cloudservice.resources.UserRegistrator;

/**
 * Restlet application called when querying whatever url except to /events.
 */
public class RestletApplication extends Application {
	
    @Override
    public synchronized Restlet createInboundRoot() {
    	
    	OAuth2.setClientCredentials();
    	String scopes[] = {"email"};
    	
    	Router router = new Router(getContext());
    	getConnectorService().getClientProtocols().add(Protocol.FILE);
    	
    	/*
    	 * When querying /login url, user is redirected to Google
    	 * authentication servers.
    	 */
    	OAuthProxy proxy = new OAuthProxy(getContext(), true);
    	proxy.setClientId(OAuth2.clientID);
    	proxy.setClientSecret(OAuth2.clientSecret);
    	proxy.setRedirectURI("https://mlha-139.sin.cvut.cz:8082/callback");
    	proxy.setAuthorizationURI("https://accounts.google.com/o/oauth2/auth");
    	proxy.setTokenURI("https://accounts.google.com/o/oauth2/token");
    	proxy.setScope(scopes);
    	//proxy.setNext(Code.class);
        router.attach("/login", proxy);
        
        router.attach("/api.json", API.class);
        
        router.attach("/", Homepage.class);
        
        Directory dir = new Directory(getContext(), "file://src/main/resources/css/");
        dir.setIndexName("css.css");
        router.attach("/css/", dir);
        
        router.attach("/app.js", JavaScript.class);
        router.attach("/css/style.css", Css.class);

        /*
         * These resources are accessible with valid authentication code only.
         */
        router.attach("/callback", Code.class);
        router.attach("/new_token", NewToken.class);
        router.attach("/user_registration", UserRegistrator.class);
        
        /*
         * This resource is accessible with valid refresh token only.
         */
        router.attach("/refresh_token", TokenRefresher.class);
        
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
