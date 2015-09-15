package cz.esc.iot.cloudservice.exceptions;

public class NoHubConnectedException extends Exception {

	private static final long serialVersionUID = -5942210878149666545L;

	public NoHubConnectedException(String msg) {
		super(msg);
	}
}
