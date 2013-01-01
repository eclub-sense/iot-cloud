package cz.cvut.felk.rest.todo.core.method;


public enum Method {

	GET(false, true),
	POST(true, true),
	PUT(true, true),
	DELETE(false, false),
	HEAD(false, false),
	OPTIONS(false, false);
	
	private final boolean requestBody;
	private final boolean responseBody;
	
	Method(boolean requestBody, boolean responseBody) {
		this.requestBody = requestBody;
		this.responseBody = responseBody;
	}
	
	public boolean isRequestBody() {
		return requestBody;
	}

	public boolean isResponseBody() {
		return responseBody;
	}
}