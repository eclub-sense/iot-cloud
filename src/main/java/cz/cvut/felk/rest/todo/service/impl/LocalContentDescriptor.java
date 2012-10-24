package cz.cvut.felk.rest.todo.service.impl;

import java.util.HashMap;
import java.util.Map;

import cz.cvut.felk.rest.todo.ContentDescriptor;

public class LocalContentDescriptor<T> implements ContentDescriptor<T> {

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContextUrl() {
		return contextUrl;
	}

	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}
}
