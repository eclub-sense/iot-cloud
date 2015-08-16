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
import cz.esc.iot.cloudservice.registry.WebSocketRegistry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (message.getType().equals("DATA") && verified == true) {
        	List<SensorEntity> sensors = ((HubDataMsg)message).getData();
        	for (SensorEntity s : sensors) {
        		SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("uuid").equal(s.getUuid()).get();
        		System.out.println(sensor);
        		MorfiaSetUp.getDatastore().update(sensor, MorfiaSetUp.getDatastore().createUpdateOperations(SensorEntity.class).unset("measured"));
        		MorfiaSetUp.getDatastore().update(sensor, MorfiaSetUp.getDatastore().createUpdateOperations(SensorEntity.class).addAll("measured", s.getData(), true));
        	}
        } else if (message.getType().equals("LOGIN")) {
        	verifyConnection(message);
        } else if (message.getType().equals("DISCOVERED") && verified == true) {
        	startStoringIntoDb((HubDiscoveredMsg)message);
        } else {
        	getSession().close(2, "Connection refused.");
        }
    }
    
    private void startStoringIntoDb(HubDiscoveredMsg message) {
    	SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("uuid").equal(message.getSensorUuid()).get();
    	System.out.println("sen: "+sensor);
    	SensorTypeInfo typeInfo = MorfiaSetUp.getDatastore().createQuery(SensorTypeInfo.class).field("type").equal(sensor.getType()).get();
    	System.out.println("info: "+typeInfo);
    	// example url: "ws://127.0.0.1:1337/servers/1111/events?topic=photocell%2F3afc5cd4-755f-422d-8585-c8d526af8e85%2Fintensity"
    	for (MeasureValue value : typeInfo.getValues()) {
    		System.out.println(value);
    		String url = "ws://127.0.0.1:1337/servers/" + message.getUuid() + "/" +"events?topic=" 
    		    + sensor.getType() + "%2F" + message.getSensorId() + "%2F" + value.getName();
    		// TODO get first values without ws
    		System.out.println(url);
    		try {
                WebsocketClient clientEndPoint = new WebsocketClient(new URI(url));
                clientEndPoint.addMsgHandler(new WebsocketClient.MsgHandler() {
                    public void handleMessage(String message) {
                    	Gson gson = new Gson();
                    	ZettaMessage zettaMsg = gson.fromJson(message, ZettaMessage.class);
                    	String measured = zettaMsg.getData();
                    	Data data = new Data();
                    	data.setName(value.getName());
                    	data.setValue(measured);
                    	System.out.println(zettaMsg);
                    	System.out.println(data);
                    	MorfiaSetUp.getDatastore().update(sensor, MorfiaSetUp.getDatastore().createUpdateOperations(SensorEntity.class).set("measured", data));
                    }
                });
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
    	}
    }
    
    private void verifyConnection(HubMessage message) {
    	System.out.println(message);
    	String hubUsername = ((HubLoginMsg)message).getUsername();
    	String hubPassword = ((HubLoginMsg)message).getPassword();
    	String hubUuid = ((HubLoginMsg)message).getUuid();
    	
    	UserEntity dbUser = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(hubUsername).field("password").equal(hubPassword).get();
    	System.out.println(dbUser);
    	System.out.println(hubUuid);
    	
    	if (dbUser != null && hubUsername.equals(dbUser.getIdentifier())
    			&& hubPassword.equals(dbUser.getPassword())) {
    		HubEntity hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("uuid").equal(hubUuid).get();
    		
    		// hub is new
    		if (hub == null) {
    			hub = new HubEntity();
    			hub.setUuid(hubUuid);
    			hub.setUser(dbUser);
    			MorfiaSetUp.getDatastore().save(hub);
        		MorfiaSetUp.getDatastore().update(dbUser, MorfiaSetUp.getDatastore().createUpdateOperations(UserEntity.class).add("hubEntities", hub, true));
        		this.hubUuid = hubUuid;
        		WebSocketRegistry.add(this);
        		Postman.sendLoginAck(this, hubUuid);
    		// in case that hub's uuid is already registered in database
    		} else {
        		this.hubUuid = hubUuid;
        		WebSocketRegistry.add(this);
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
    	WebSocketRegistry.remove(this);
        map.remove(this.hubUuid);
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        System.out.println("len: "+WebSocketRegistry.size());
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
