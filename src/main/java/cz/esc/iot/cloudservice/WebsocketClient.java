package cz.esc.iot.cloudservice;
import java.net.URI;
import java.util.Map;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

/**
 * This class connects to zetta-cloud. Then sensor's measured value is send through
 * websocket into database.
 */
public class WebsocketClient extends Endpoint {
	
    Session userSession = null;
    private MsgHandler messageHandler;
    private long lastTimestamp = 0;
    
    public WebsocketClient(URI endpointURI) {
        try {
        	ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
        		@Override
        		public void beforeRequest(Map<String,List<String>> headers) {
        			/*List<String> user = new LinkedList<>();
        			List<String> password = new LinkedList<>();
        			user.add("admin");
        			password.add("admin");
        	        headers.put("User", user);
        	        headers.put("Password", password);*/
        	    }
        	};
        	ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create()
        	        .configurator(configurator)
        	        .build();
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.setDefaultMaxSessionIdleTimeout(0);
            System.out.println("connecting...");
            container.connectToServer(this, clientConfig, endpointURI);
            System.out.println("connected!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(Session arg0, EndpointConfig arg1) {
        System.out.println("...opening websocket");
        this.userSession = arg0;
        userSession.addMessageHandler(new MessageHandler.Whole<String>() {

        	@Override
            public void onMessage(String message) {
                if (messageHandler != null) {
                    messageHandler.handleMessage(message);
                }
            }
 
        });
    }

    @Override
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("...closing websocket");
        this.userSession = null;
    }
    
    public void addMsgHandler(MsgHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public long getLastTimestamp() {
		return lastTimestamp;
	}

	public void setLastTimestamp(long lastTimestamp) {
		this.lastTimestamp = lastTimestamp;
	}

	public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MsgHandler {
        public void handleMessage(String message);
    }
}