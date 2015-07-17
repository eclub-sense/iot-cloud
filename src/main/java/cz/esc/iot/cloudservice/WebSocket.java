package cz.esc.iot.cloudservice;

import java.util.ArrayList;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.messages.HubDataMsg;
import cz.esc.iot.cloudservice.messages.HubLoginMsg;
import cz.esc.iot.cloudservice.messages.HubMessage;
import cz.esc.iot.cloudservice.messages.HubMessageType;
import cz.esc.iot.cloudservice.messages.MessageInstanceCreator;
import cz.esc.iot.cloudservice.registry.ConnectedSensorList;
import cz.esc.iot.cloudservice.sensors.Sensor;

public class WebSocket extends WebSocketAdapter
{
	private static ArrayList<WebSocket> sockets = new ArrayList<WebSocket>();
	private boolean verified = false;
	
	public static WebSocket getInstance(int hubNum) {
		return sockets.get(hubNum);
	}
	
    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        sockets.add(this);
        System.out.println("Socket Connected: " + sess);
        //System.out.println(ConnectedSensorList.getInstance().getList());
    }
    
    @Override
    public void onWebSocketText(String json)
    {
        super.onWebSocketText(json);
        System.out.println("Received TEXT message: " + json);
        HubMessage message = MessageInstanceCreator.createMsgInstance(json);
        
        if (message.getType() == HubMessageType.DATA && verified == true) {
        	Sensor sensor = ConnectedSensorList.getInstance().getSensor(message.getIntUuid());
        	sensor.setMessageParts(((HubDataMsg)message).getData());
        	System.out.println(sensor);
        	System.out.println(message);
        } else if (message.getType() == HubMessageType.LOGIN) {
        	System.out.println(message);
        	if ((((HubLoginMsg)message).getUsername().equals(RestletApplication.username)) 
        			&& (((HubLoginMsg)message).getPassword().equals(RestletApplication.password))) {
        		// TODO register new hub
        		verified = true;
        	} else {
        		getSession().close(1, "Incorrect username or password.");
        	}
        } else {
        	getSession().close(2, "Connection refused.");
        }
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
    	sockets.remove(this);
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }    
	
    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len)
    {
        super.onWebSocketBinary(payload, offset, len);
        System.out.print("Received binary message: ");
        String msg = new String(payload);
        System.out.println(msg);
    }
    
    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
