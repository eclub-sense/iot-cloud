package cz.cvut.felk.rest.todo.core;

import cz.cvut.felk.rest.todo.core.method.Method;

public class RequestHolder<T> implements Request<T> {

	private Method method;
	
	public RequestHolder(String uri) {
		super();
	}
	
	public void setMethod(Method method) {
		this.method = method;
	}

	@Override
	public Object getMeta(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setBody(T body) {
		
	}
	
	public void setUri(String uri) {
		
	}
	
	@Override
	public T getBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getMetaNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Method getMethod() {
		return method;
	}

}
