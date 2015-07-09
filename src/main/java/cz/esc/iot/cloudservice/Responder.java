package cz.esc.iot.cloudservice;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;

public class Responder {

	private Session sess;
	
	public Responder(Session sess) {
		this.sess = sess;
	}
	
	public void sendAck(String info) {
		try {
			sess.getRemote().sendString("ACK_"+info);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
