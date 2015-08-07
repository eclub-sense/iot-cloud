package cz.esc.iot.cloudservice;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import cz.esc.iot.cloudservice.messages.*;
import cz.esc.iot.cloudservice.persistance.dao.MorfiaSetUp;
import cz.esc.iot.cloudservice.persistance.model.HubEntity;
import cz.esc.iot.cloudservice.persistance.model.MeasuredValues;
import cz.esc.iot.cloudservice.persistance.model.SensorEntity;
import cz.esc.iot.cloudservice.persistance.model.UserEntity;
import cz.esc.iot.cloudservice.registry.ConnectedHubRegistry;
import cz.esc.iot.cloudservice.registry.WebSocketRegistry;

public class WebSocket extends WebSocketAdapter {
	
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
        HubMessage message = MessageInstanceCreator.createMsgInstance(json);
        if (message.getType().equals("DATA") && verified == true) {
        	//Sensor sensor = ConnectedSensorRegistry.getInstance().get(message.getIntUuid());
        	SensorEntity sensor = MorfiaSetUp.getDatastore().createQuery(SensorEntity.class).field("uuid").equal(message.getUuid()).get();
        	//sensor.readPacket(((HubDataMsg)message).getData());
        	if (sensor != null) {
        		MeasuredValues values = MorfiaSetUp.getDatastore().createQuery(MeasuredValues.class).field("sensor").equal(sensor).get();
        		MorfiaSetUp.getDatastore().update(values, MorfiaSetUp.getDatastore().createUpdateOperations(MeasuredValues.class).unset("data"));
        		MorfiaSetUp.getDatastore().update(values, MorfiaSetUp.getDatastore().createUpdateOperations(MeasuredValues.class).addAll("data", ((HubDataMsg)message).getData(), true));
        	}
        } else if (message.getType().equals("LOGIN")) {
        	verifyConnection(message);
        } else {
        	getSession().close(2, "Connection refused.");
        }
        System.out.println(ConnectedHubRegistry.getInstance().getList());
    }
    
    private void verifyConnection(HubMessage message) {
    	System.out.println(message);
    	String hubUsername = ((HubLoginMsg)message).getUsername();
    	String hubPassword = ((HubLoginMsg)message).getPassword();
    	String hubUuid = ((HubLoginMsg)message).getUuid();
    	
    	UserEntity dbUser = MorfiaSetUp.getDatastore().createQuery(UserEntity.class).field("username").equal(hubUsername).field("password").equal(hubPassword).get();
    	System.out.println(dbUser);
    	System.out.println(hubUuid);
    	
    	if (dbUser != null && hubUsername.equals(dbUser.getUsername())
    			&& hubPassword.equals(dbUser.getPassword())) {
    		HubEntity hub = MorfiaSetUp.getDatastore().createQuery(HubEntity.class).field("uuid").equal(hubUuid).get();
    		
    		// hub is new
    		if (hub == null) {
    			hub = new HubEntity();
    			hub.setUuid(hubUuid);
    			MorfiaSetUp.getDatastore().save(hub);
        		MorfiaSetUp.getDatastore().update(dbUser, MorfiaSetUp.getDatastore().createUpdateOperations(UserEntity.class).add("hubEntities", hub, true));
    			Postman.sendLoginAck(this, hubUuid);
    		// in case that hub's uuid is already registered in database
    		} else {
    			Postman.sendLoginAck(this, hubUuid);
        		try {
					Postman.reregisterAllSensors(this, hubUuid);
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    		this.hubUuid = hubUuid;
    		WebSocketRegistry.add(this);
    		verified = true;
    	} else {
    		getSession().close(1, "Incorrect username or password.");
    	}
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason) {
    	WebSocketRegistry.remove(this);
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
