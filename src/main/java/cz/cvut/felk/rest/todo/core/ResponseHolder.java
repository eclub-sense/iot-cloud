package cz.cvut.felk.rest.todo.core;

import java.util.HashMap;
import java.util.Map;

public class ResponseHolder<T> implements Response<T> {

	private String url;
	private String contextUrl;
	
	private T body;
	private Map<String, Object> metas = new HashMap<String, Object>();
	
	
	
	public Object getMeta(String name) {
		return metas.get(name);
	}

	public void setMeta(String name, Object value) {
		metas.put(name, value);
	}
	
	public T getBody() {
		return body;
	}
	
	public void setBody(T body) {
		this.body = body;
	}

	public String[] getMetaNames() {
		return metas.keySet().toArray(new String[0]);
	}

	public String getUri() {
		return url;
	}

	public void setUri(String url) {
		this.url = url;
	}

	public String getContext() {
		return contextUrl;
	}

	public void setContext(String contextUrl) {
		this.contextUrl = contextUrl;
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}
}
