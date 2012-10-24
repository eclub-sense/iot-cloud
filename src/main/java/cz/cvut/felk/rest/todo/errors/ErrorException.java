package cz.cvut.felk.rest.todo.errors;


public class ErrorException extends Exception {

	private static final long serialVersionUID = 1531552972478130773L;
	
	private final int statusCode;

	public ErrorException(int statusCode) {
		super();
		this.statusCode = statusCode;
	}
	
	public ErrorException(int statusCode, Throwable cause) {
		super(cause);
		this.statusCode = statusCode;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
}
