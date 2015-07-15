package cz.esc.iot.cloudservice;

import java.util.ArrayList;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class WebSocket extends WebSocketAdapter
{
	private static ArrayList<WebSocket> sockets = new ArrayList<WebSocket>();
	//private static WebSocket socket;
	
	/*public WebSocket() {
		socket = this;
	}*/
	
	public static WebSocket getInstance(int hubNum) {
		return sockets.get(hubNum);
		//return socket;
	}
	
    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        
        //System.out.println(sockets);
        System.out.println("Socket Connected: " + sess);
        //System.out.println(ConnectedSensorList.getInstance().getList());
    }
    
    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        //System.out.println(sockets);
        System.out.println("Received TEXT message: " + message);
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
    	//System.out.println(sockets);
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
