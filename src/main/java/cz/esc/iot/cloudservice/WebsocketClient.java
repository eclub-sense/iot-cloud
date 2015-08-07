package cz.esc.iot.cloudservice;
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class WebsocketClient {

	
	/*
	 * TODO authentication
	 * 
	 * 
	 * URI uri = new URI("...");
ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
    public void beforeRequest(Map> headers) {
        headers.put("Authorization", asList("Basic " + DatatypeConverter.printBase64Binary("user:password".getBytes())));
    }
};
ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create()
        .configurator(configurator)
        .build();

WebSocketContainer container = ContainerProvider.getWebSocketContainer();
Session client = container.connectToServer(new MyEndpoint(), clientConfig, uri);

// do something

client.close();


	 */
	
    Session userSession = null;
    private MessageHandler messageHandler;

    public WebsocketClient(URI endpointURI) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        System.out.println("...opening websocket");
        this.userSession = userSession;
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("...closing websocket");
        this.userSession = null;
    }
    
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MessageHandler {
        public void handleMessage(String message);
    }
}