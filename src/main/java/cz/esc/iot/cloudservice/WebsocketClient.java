package cz.esc.iot.cloudservice;
import java.net.URI;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

public class WebsocketClient extends Endpoint {
	
    Session userSession = null;
    private MsgHandler messageHandler;

    public WebsocketClient(URI endpointURI) {
        try {
        	ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
        		@Override
        		public void beforeRequest(Map<String,List<String>> headers) {
        			List<String> user = new LinkedList<>();
        			List<String> password = new LinkedList<>();
        			user.add("admin");
        			password.add("admin");
        	        headers.put("User", user);
        	        headers.put("Password", password);
        	    }
        	};
        	ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create()
        	        .configurator(configurator)
        	        .build();
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, clientConfig, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MsgHandler {
        public void handleMessage(String message);
    }
}