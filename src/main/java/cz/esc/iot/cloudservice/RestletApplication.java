package cz.esc.iot.cloudservice;

import cz.esc.iot.cloudservice.resources.Homepage;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.ext.oauth.OAuthAuthorizer;
import org.restlet.ext.oauth.OAuthParameters;
import org.restlet.ext.oauth.OAuthProxy;
import org.restlet.ext.oauth.internal.Scopes;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.Verifier;

import cz.esc.iot.cloudservice.resources.UserRegistrator;
import cz.esc.iot.cloudservice.resources.RegisteredHubs;
import cz.esc.iot.cloudservice.resources.RegisteredSensors;
import cz.esc.iot.cloudservice.resources.SensorRegistrator;
import cz.esc.iot.cloudservice.resources.ShareSensor;

public class RestletApplication extends Application {
	
    @Override
    public synchronized Restlet createInboundRoot() {
    	
        
        String scopes[] = {"email"};
        
        OAuthParameters params = new OAuthParameters(clientID, clientSecret,
        		"https://accounts.google.com/o/oauth2/", Scopes.toRoles("email"));
	    params.setAccessTokenPath("token");
	    params.setAuthorizePath("auth");
        OAuthProxy proxy = new OAuthProxy(params, getContext().createChildContext(), true);
        proxy.setNext(Homepage.class);
        
        /*OAuthProxy root = new OAuthProxy(getContext().createChildContext(), true);
        root.setClientId(clientID);
        root.setClientSecret(clientSecret);
        root.setRedirectURI("http://localhost:8081/callback");
        root.setAuthorizationURI("https://accounts.google.com/o/oauth2/auth");
        root.setTokenURI("https://accounts.google.com/o/oauth2/token");
        root.setScope(scopes);
        root.setNext(Homepage.class);*/

        
        Router router = new Router(getContext());

        router.attach("/", proxy);
        //router.attach("/callback", Homepage.class);
        router.attach("/sensor_registration", SensorRegistrator.class); 
        router.attach("/registered_sensors/{uuid}", RegisteredSensors.class);
        router.attach("/registered_sensors", RegisteredSensors.class);
        router.attach("/registered_hubs/{uuid}", RegisteredHubs.class);
        router.attach("/registered_hubs", RegisteredHubs.class);
        router.attach("/user_registration", UserRegistrator.class);
        router.attach("/share_sensor", ShareSensor.class);
        return router;
    }
}
