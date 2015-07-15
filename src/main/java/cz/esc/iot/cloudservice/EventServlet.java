package cz.esc.iot.cloudservice;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
public class EventServlet extends WebSocketServlet
{	
    @Override
    public void configure(WebSocketServletFactory factory)
    {
        factory.register(WebSocket.class);
    }
}