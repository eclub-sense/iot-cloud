package cz.cvut.felk.rest.todo.servlet;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import cz.cvut.felk.rest.todo.ContentDescriptor;
import cz.cvut.felk.rest.todo.dto.TodoItemDto;
import cz.cvut.felk.rest.todo.errors.ErrorException;

public class HttpRequest<T> implements ContentDescriptor<T> {

	private HttpServletRequest httpRequest;
	
	private T body;
	private String url;
	
	protected HttpRequest(String url, HttpServletRequest httpRequest) { 
		this(url, null,  httpRequest);
	}
	
	protected HttpRequest(String url, T body, HttpServletRequest httpRequest) {
		super();
		this.httpRequest = httpRequest;
		this.body = body;
		this.url = url;
	}
	
	public static String getUrl(final HttpServletRequest httpRequest) {
		final String context = httpRequest.getContextPath();
		
		return (context != null) 
						? httpRequest.getRequestURI().substring(context.length())
						: httpRequest.getRequestURI();		
	}
	
	
    public static <T> HttpRequest<T> read(final Class<? extends T> clazz, HttpServletRequest httpRequest, String url) throws ErrorException {
    	
    	final String contentType = (String) HttpRequest.getMeta(ContentDescriptor.META_CONTENT_TYPE, httpRequest);
    	
    	if ((contentType != null) && (clazz != null) && (!Void.class.equals(clazz))) {
    		final Gson gson = new Gson();
			try {
				
				if (contentType.startsWith(CT_ITEM)) {
					if (TodoItemDto.class.equals(clazz)) {
						return new HttpRequest<T>(
									url,
									gson.fromJson(httpRequest.getReader(), clazz),
									httpRequest
								);
					}
				}
				throw new ErrorException(HttpServletResponse.SC_BAD_REQUEST);				
				
			} catch (JsonSyntaxException e) {
				throw new ErrorException(HttpServletResponse.SC_BAD_REQUEST, e);
				
			} catch (JsonIOException e) {
				throw new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
				
			} catch (IOException e) {
				throw new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
			}

    	}
		return new HttpRequest<T>(url, httpRequest);
    }
	
	public String getUrl() {
		return url;
	}
	
	public T getBody() {
		return body;
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
		return getMeta(name, httpRequest);
	}
	
	public String[] getMetaNames() {
		return Collections.list(httpRequest.getHeaderNames()).toArray(new String[0]);
	}

	public String getContextUrl() {
		return httpRequest.getContextPath();
	}
}
