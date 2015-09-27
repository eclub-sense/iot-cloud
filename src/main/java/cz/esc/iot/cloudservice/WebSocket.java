package cz.esc.iot.cloudservice;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import com.google.gson.Gson;

import cz.esc.iot.cloudservice.messages.*;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.Data;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.MeasureValue;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.persistance.model.SensorTypeInfo;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import cz.esc.iot.cloudservice.support.WebSocketRegistry;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class listening and handling websocket events.
 */
public class WebSocket extends WebSocketAdapter {
    
    private static Map<String, WebSocket> map = new HashMap<>();
    
    public static WebSocket getWebSocketByUuid(String uuid) {
        return map.get(uuid);
    }
    
    public static Map<String, WebSocket> getAllWebSockets() {
        return map;
    }
	
	private boolean verified = false;
	private String hubUuid;
	
    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        System.out.println("Socket Connected: " + sess);
    }
    
    @Override
    public void onWebSocketText(String json) {
    	super.onWebSocketText(json);
        System.out.println("Received TEXT message: " + json);
        HubMessage message;
		try {
			message = MessageInstanceCreator.createMsgInstance(json);
		} catch (Exception e) {
			getSession().close(2, "Connection refused.");
			e.printStackTrace();
			return;
		}
		
		// Continue according to message type...
		
        if (message.getType().equals("DATA") && verified == true) {
        	// UNSUPPORTED
        } else if (message.getType().equals("LOGIN")) {
        	verifyConnection(message);
        } else if (message.getType().equals("DISCOVERED") && verified == true) {
        	startStoringIntoDb((HubDiscoveredMsg)message);
        	System.out.println("DISCOVERED: " + ((HubDiscoveredMsg)message).getSensorUuid());
        } else {
        	getSession().close(2, "Connection refused.");
        }
    }
    
    /**
     * When DISCOVERED message is obtained from hub, database starts storing
     * sensor's data.
     */
    private void startStoringIntoDb(HubDiscoveredMsg message) {
    	SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("uuid").equal(message.getSensorUuid()).get();
    	SensorTypeInfo typeInfo = MorfiaSetUp.getDatastore().createQuery(SensorTypeInfo.class).field("type").equal(sensor.getType()).get();
    	
    	// example url: "ws://127.0.0.1:1337/servers/1111/events?topic=photocell%2F3afc5cd4-755f-422d-8585-c8d526af8e85%2Fintensity"
    	
    	// TODO get first values without ws
    	// TODO get measuring values from siren not from database
    	for (MeasureValue value : typeInfo.getValues()) {
    		System.out.println(value);
    		String url = "ws://127.0.0.1:1337/servers/" + message.getUuid() + "/" +"events?topic=" 
    		    + sensor.getType() + "%2F" + message.getSensorId() + "%2F" + value.getName();
    		System.out.println(url);
    		try {
                WebsocketClient clientEndPoint = new WebsocketClient(new URI(url));
                clientEndPoint.addMsgHandler(new WebsocketClient.MsgHandler() {
                    public void handleMessage(String message) {
                    	Gson gson = new Gson();
                    	ZettaMessage zettaMsg = gson.fromJson(message, ZettaMessage.class);
                    	String measured = zettaMsg.getData();
                    	long timestamp = zettaMsg.getTimestamp();
                    	
                    	if (timestamp - clientEndPoint.getLastTimestamp() >= 10) {
                    		clientEndPoint.setLastTimestamp(timestamp);
                    		Data data = new Data();
	                    	data.setName(value.getName());
	                    	data.setValue(measured);
	                    	data.setTime(new Date(timestamp));
	                    	data.setSensor(sensor);
	                    	//System.out.println(data);
	                    	MorfiaSetUp.getDatastore().save(data);
                    	}
                    }
                });
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
    	}
    }
    
    /**
     * Verifying user after LOGIN message was obtained.
     */
    private void verifyConnection(HubMessage message) {
    	String hubMail = ((HubLoginMsg)message).getEmail();
    	String hubUuid = ((HubLoginMsg)message).getUuid();
    	String hubPassword = ((HubLoginMsg)message).getPassword();

    	if (hubMail.equals("admin") && hubPassword.equals("") && WebSocketRegistry.getCloudSocket() == null) {
			HubEntity hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("uuid").equal(hubUuid).get();
			if (hub == null) {
				hub = new HubEntity();
				hub.setUuid(hubUuid);
				hub.setStatus("connected");
				MorfiaSetUp.getDatastore().save(hub);
			}
    		this.hubUuid = hubUuid;
    		MorfiaSetUp.getDatastore().update(hub, MorfiaSetUp.getDatastore().createUpdateOperations(HubEntity.class).set("status", "connected"));
    		WebSocketRegistry.setCloudSocket(this);
    		Postman.sendLoginAck(this, hubUuid);
    		try {
				Postman.reregisterAllSensors(this, hubUuid);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		verified = true;
    		return;
    	}
		UserEntity dbUser = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("email").equal(hubMail).field("password").equal(hubPassword).get();
		System.out.println("User: " + dbUser);
		if (dbUser == null) {
			getSession().close(3, "Forbidden");
		}
    	
    	if (dbUser != null) {
    		HubEntity hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("uuid").equal(hubUuid).get();
    		
    		// hub is new
    		if (hub == null) {
    			hub = new HubEntity();
    			hub.setUuid(hubUuid);
    			hub.setUser(dbUser);
    			hub.setStatus("connected");
    			MorfiaSetUp.getDatastore().save(hub);
        		this.hubUuid = hubUuid;
        		if (hubUuid.charAt(0) != 'm') {
        			WebSocketRegistry.add(this);
        			Postman.sendLoginAck(this, hubUuid);
        		} else
        			Postman.sendLoginAck(WebSocketRegistry.getCloudSocket(), hubUuid);
    		// in case that hub's uuid is already registered in database
    		} else {
    			if (!hub.getUser().equals(dbUser)) {
    				getSession().close(3, "Forbidden");
    			}
    			MorfiaSetUp.getDatastore().update(hub, MorfiaSetUp.getDatastore().createUpdateOperations(HubEntity.class).set("status", "connected"));
        		if(hubUuid.charAt(0) != 'm') {
        			this.hubUuid = hubUuid;
        			WebSocketRegistry.add(this);
        		}
    			Postman.sendLoginAck(this, hubUuid);
        		try {
					Postman.reregisterAllSensors(this, hubUuid);
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    		verified = true;
            map.put(hubUuid, this);
    		
    	} else {
    		getSession().close(1, "Incorrect username or password.");
    	}
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason) {
    	HubEntity hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("uuid").equal(this.hubUuid).get();
    	if (this.hubUuid.equals("00000000"))
    		WebSocketRegistry.setCloudSocket(null);
    	else
    		WebSocketRegistry.remove(this);
    	MorfiaSetUp.getDatastore().update(hub, MorfiaSetUp.getDatastore().createUpdateOperations(HubEntity.class).set("status", "disconnected"));
        map.remove(this.hubUuid);
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }    
	
    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len) {
        super.onWebSocketBinary(payload, offset, len);
        System.out.print("Received binary message: ");
        String msg = new String(payload);
        System.out.println(msg);
    }
    
    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public String getHubUuid() {
		return hubUuid;
	}

	public void setHubUuid(String hubUuid) {
		this.hubUuid = hubUuid;
	}
}
