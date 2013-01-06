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
package cz.cvut.felk.rest.todo.api.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.CopyUtils;

import cz.cvut.felk.rest.todo.api.ErrorException;
import cz.cvut.felk.rest.todo.api.Request;
import cz.cvut.felk.rest.todo.api.RequestHolder;
import cz.cvut.felk.rest.todo.api.ResourceDescriptor;
import cz.cvut.felk.rest.todo.api.Response;
import cz.cvut.felk.rest.todo.api.content.ContentAdapter;
import cz.cvut.felk.rest.todo.api.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.api.content.ContentHolder;
import cz.cvut.felk.rest.todo.api.http.headers.HttpAcceptHeader;
import cz.cvut.felk.rest.todo.api.http.lang.HttpDate;
import cz.cvut.felk.rest.todo.api.method.Method;
import cz.cvut.felk.rest.todo.api.method.MethodDescriptor;

public abstract class HttpServlet extends GenericServlet {

	private static final long serialVersionUID = 5919203442935383094L;

	protected abstract ResourceDescriptor resolve(String uri);
	
	protected void doService(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException, IOException {
		
		final RequestHolder<Object> request = new RequestHolder<Object>(getUri(httpRequest));
		request.setContext(httpRequest.getContextPath());
		
		// Resolve incoming URL and get resource descriptor
		final ResourceDescriptor resourceDsc = resolve(request.getUri());
		
		// Does the requested resource exist?
		if (resourceDsc == null) {	
			throw new ErrorException(HttpServletResponse.SC_NOT_FOUND);
		}
		
		// Is requested method supported?
		if (httpRequest.getMethod() == null) {
			throw new ErrorException(HttpServletResponse.SC_BAD_REQUEST);
		}
			
		try {
			request.setMethod(Method.valueOf(httpRequest.getMethod().toUpperCase(Locale.US)));
			
		} catch (IllegalArgumentException ex) {
			throw new ErrorException(HttpServletResponse.SC_NOT_IMPLEMENTED);			
		}
		
		if (request.getMethod() == null) {
			throw new ErrorException(HttpServletResponse.SC_NOT_IMPLEMENTED);
		}

		// Get supported methods for requested resource
		Map<Method, MethodDescriptor<?, ?>> methods = resourceDsc.methods();
		
		// Get requested method descriptors for the resource
		MethodDescriptor<?, ?> methodDsc = (methods != null) ? methods.get(request.getMethod()) : null;

		// Is requested method supported?
		if ((methodDsc == null)) {
			throw new ErrorException(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}

		ContentAdapter<InputStream, ?> inputContentAdapter = null;
		
		// Is request body expected?
		if (request.getMethod().isRequestBody()) {
			String requestContentType = httpRequest.getContentType();
			
			inputContentAdapter = (methodDsc.consumes() != null) ? methodDsc.consumes().get(requestContentType) : null;
			if (inputContentAdapter == null) {
				throw new ErrorException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);		
			}
			
		} else if (httpRequest.getContentLength() > 0){
			// Unexpected request body
			throw new ErrorException(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		ContentAdapter<?, InputStream> outputContentAdapter = null;

		String responseContentType = null;
		
		// Is response body expected?
		if (request.getMethod().isResponseBody()) {
			// Check Accept header
			HttpAcceptHeader acceptHeader = HttpAcceptHeader.read(httpRequest.getHeader(ContentDescriptor.META_ACCEPT));
			if (acceptHeader != null) {
				
				Map<String, ?> produces = methodDsc.produces();

				// Response content negotiation 
				if (produces != null) {
					int weight = 0;
					
					for (String ct : produces.keySet()) {
						int tw = acceptHeader.accept(ct); 
						if (tw > weight) {
							weight = tw;
							responseContentType = ct;
							outputContentAdapter = (ContentAdapter<?, InputStream>) produces.get(ct);
						}
					}
				}
				if (outputContentAdapter == null) {
					throw new ErrorException(HttpServletResponse.SC_NOT_ACCEPTABLE);
				}
			}
		}
		
		if (inputContentAdapter != null) {
			ContentHolder<Object> lc = new ContentHolder<Object>();
			lc.setBody(inputContentAdapter.transform(httpRequest.getInputStream()));
			request.setContent(lc);
		}
		
		// Invoke resource method
		Response response = methodDsc.invoke((Request) request);
		
		if (response == null) {
			throw new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		// Write response status
		int responseStatus = (response.getStatus() > 0) ? response.getStatus() : HttpServletResponse.SC_OK;
		httpResponse.setStatus(responseStatus);
		
		if (response.getContent() == null) {
			return;
		}
		
		// Write response headers
		if (response.getContent().getMetaNames() != null) {
			for (String metaName : response.getContent().getMetaNames()) {
				Object metaValue = response.getContent().getMeta(metaName);
				if (metaValue != null) {
					if (metaValue instanceof Date) {
						httpResponse.setHeader(metaName, HttpDate.RFC1123_FORMAT.format(((Date)metaValue)));	
					} else {
						httpResponse.setHeader(metaName, metaValue.toString());
					}
				}
			}
		}

		if ((HttpServletResponse.SC_CREATED == responseStatus)) {
			httpResponse.setHeader(ContentDescriptor.META_LOCATION, response.getContext() + response.getUri());
		} 

		if ((response.getContent().getBody() == null) || (HttpServletResponse.SC_NOT_MODIFIED == responseStatus)) {
			return;
		}
		
		// Write response body
		if (outputContentAdapter != null) {
			httpResponse.setHeader(ContentDescriptor.META_CONTENT_TYPE, responseContentType);
			InputStream is =  ((ContentAdapter<Object, InputStream>)outputContentAdapter).transform(response.getContent().getBody());
			if (is != null) {
				CopyUtils.copy(is, httpResponse.getOutputStream());
			}
		}
	}
	
	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {

		HttpServletRequest nativeHttpRequest = null;
		HttpServletResponse nativeHttpResponse = null;
        
		try {
			nativeHttpRequest = (HttpServletRequest) servletRequest;
			nativeHttpResponse = (HttpServletResponse) servletResponse;
			
			doService(nativeHttpRequest, nativeHttpResponse);
			
		} catch (ClassCastException e) {
			throw new ServletException("non-HTTP request or response");
			
		} catch (ErrorException e) {
			if (nativeHttpResponse != null) {
				handleError(e, nativeHttpResponse);
				return;
			} 
			throw new ServletException(e);
		} catch (Exception e) {
			if (nativeHttpResponse != null) {
				handleError(new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e), nativeHttpResponse);
				return;
			} 
			throw new ServletException(e);			
		}
	}

	protected abstract void handleError(ErrorException ex, HttpServletResponse httpResponse);
	
	protected static String getUri(final HttpServletRequest httpRequest) {
		final String context = httpRequest.getContextPath();

		return (context != null) 
						? httpRequest.getRequestURI().substring(context.length())
						: httpRequest.getRequestURI();		
	}
}
