/*
 *  Copyright 2012 sprintapi.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cz.cvut.felk.rest.todo.servlet;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

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
