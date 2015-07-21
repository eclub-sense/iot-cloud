package cz.esc.iot.cloudservice;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import cz.esc.iot.cloudservice.messages.HubDataMsg;
import cz.esc.iot.cloudservice.messages.HubLoginMsg;
import cz.esc.iot.cloudservice.messages.HubMessage;
import cz.esc.iot.cloudservice.messages.HubMessageType;
import cz.esc.iot.cloudservice.messages.MessageInstanceCreator;
import cz.esc.iot.cloudservice.messages.Postman;
import cz.esc.iot.cloudservice.registry.ConnectedHubRegistry;
import cz.esc.iot.cloudservice.registry.ConnectedSensorRegistry;
import cz.esc.iot.cloudservice.sensors.Sensor;
import cz.esc.iot.cloudservice.hubs.Hub;

public class WebSocket extends WebSocketAdapter {
	
	private boolean verified = false;
	
    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        System.out.println("Socket Connected: " + sess);
        System.out.println(ConnectedHubRegistry.getInstance().getList());
    }
    
    @Override
    public void onWebSocketText(String json) {
        super.onWebSocketText(json);
        System.out.println("Received TEXT message: " + json);
        HubMessage message = MessageInstanceCreator.createMsgInstance(json);
        if (message.getType() == HubMessageType.DATA && verified == true) {
        	Sensor sensor = ConnectedSensorRegistry.getInstance().get(message.getIntUuid());
        	sensor.readPacket(((HubDataMsg)message).getData());
        } else if (message.getType() == HubMessageType.LOGIN) {
        	verifyConnection(message);
        } else {
        	getSession().close(2, "Connection refused.");
        }
        System.out.println(ConnectedHubRegistry.getInstance().getList());
    }
    
    private void verifyConnection(HubMessage message) {
    	System.out.println(message);
    	if ((((HubLoginMsg)message).getUsername().equals(RestletApplication.username)) 
    			&& (((HubLoginMsg)message).getPassword().equals(RestletApplication.password))) {
    		Hub hub = ConnectedHubRegistry.getInstance().get(message.getIntUuid());
    		if (hub == null) {
    			hub = new Hub(message.getIntUuid(), this);
    			ConnectedHubRegistry.getInstance().add(hub);
    			Postman.sendLoginAck(hub);
    		} else {
    			hub.setSocket(this);
    			Postman.sendLoginAck(hub);
        		try {
					Postman.reregisterAllSensors(hub);
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    		verified = true;
    	} else {
    		getSession().close(1, "Incorrect username or password.");
    	}
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason) {
    	ConnectedHubRegistry.getInstance().whoHasSocket(this).setSocket(null);
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        System.out.println(ConnectedHubRegistry.getInstance().getList());
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
}
