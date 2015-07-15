package cz.esc.iot.cloudservice;

import java.util.ArrayList;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cz.esc.iot.cloudservice.messages.HubMessage;
import cz.esc.iot.cloudservice.registry.ConnectedSensorList;
import cz.esc.iot.cloudservice.sensors.Sensor;

public class WebSocket extends WebSocketAdapter
{
	private static ArrayList<WebSocket> sockets = new ArrayList<WebSocket>();
	
	public static WebSocket getInstance(int hubNum) {
		return sockets.get(hubNum);
	}
	
    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        System.out.println("Socket Connected: " + sess);
        //System.out.println(ConnectedSensorList.getInstance().getList());
    }
    
    @Override
    public void onWebSocketText(String json)
    {
        super.onWebSocketText(json);
        System.out.println("Received TEXT message: " + json);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        HubMessage message = gson.fromJson(json, HubMessage.class);
        Sensor sensor = ConnectedSensorList.getInstance().getSensor(message.getUuid());
        sensor.setBinaryData(message.getEncrypted());
        /*try {
        	System.out.println("odeslano");
			session.getRemote().sendString("Ahoj, Michale");
		} catch (IOException e) {
			e.printStackTrace();
		}*/
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
        for (int i = 0; i < len; i++) {
        	System.out.printf("%X", payload[i]);
        }
        System.out.print('\n');
    }
    
    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
}
