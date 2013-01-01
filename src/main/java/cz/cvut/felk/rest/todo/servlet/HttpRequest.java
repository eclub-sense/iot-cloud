package cz.cvut.felk.rest.todo.servlet;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import cz.cvut.felk.rest.todo.core.Request;
import cz.cvut.felk.rest.todo.core.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.core.method.Method;

public class HttpRequest {

	private HttpServletRequest nativeHttpRequest;
	
	private String url;
	private Method method;
	
	protected HttpRequest(String url, Method method, HttpServletRequest nativeHttpRequest) { 
		super();
		this.url = url;
		this.method = method;
		this.nativeHttpRequest = nativeHttpRequest;
	}
		
	public static String getUri(final HttpServletRequest httpRequest) {
		final String context = httpRequest.getContextPath();
		
		return (context != null) 
						? httpRequest.getRequestURI().substring(context.length())
						: httpRequest.getRequestURI();		
	}
	
	public String getUri() {
		return url;
	}
	
	public InputStream getBody() {
return null;
	}

	public static Object getMeta(final String name, final HttpServletRequest httpRequest) {
		String value = httpRequest.getHeader(name); 
		if (value != null) {
			if (ContentDescriptor.META_IF_MODIFIED_SINCE.equalsIgnoreCase(name)) {
				
				try {
					return (new SimpleDateFormat("dd MMM yyyy HH:mm:ss zzz")).parse(value);
				} catch (ParseException e) {
					return value.toString();
				}
			}
			return value.toString();
		}
		return null;
	}
	
	public Object getMeta(String name) {
		return getMeta(name, nativeHttpRequest);
	}
	
	public String[] getMetaNames() {
		return Collections.list(nativeHttpRequest.getHeaderNames()).toArray(new String[0]);
	}

	public String getContext() {
		return nativeHttpRequest.getContextPath();
	}
}
