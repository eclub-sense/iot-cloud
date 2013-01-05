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

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.CopyUtils;

import cz.cvut.felk.rest.todo.core.RequestHolder;
import cz.cvut.felk.rest.todo.core.ResourceDescriptor;
import cz.cvut.felk.rest.todo.core.Response;
import cz.cvut.felk.rest.todo.core.UrlResolver;
import cz.cvut.felk.rest.todo.core.content.ContentAdapter;
import cz.cvut.felk.rest.todo.core.content.ContentDescriptor;
import cz.cvut.felk.rest.todo.core.method.Method;
import cz.cvut.felk.rest.todo.core.method.MethodDescriptor;
import cz.cvut.felk.rest.todo.errors.ErrorException;
import cz.cvut.felk.rest.todo.http.headers.HttpDate;

public class TodoAppServlet  extends GenericServlet {

	private static final long serialVersionUID = 4650079506676226041L;

	private UrlResolver resolver = null;
	
	@Override
	public void init() throws ServletException {
		super.init();
	}
	
	protected void doService(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ErrorException, IOException {
		
		final RequestHolder<Object> request = new RequestHolder<Object>(HttpRequest.getUri(httpRequest));
		
		// Resolve incoming URL and get resource descriptor
		final ResourceDescriptor resourceDsc = resolver.resolve(request.getUri());
		
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
		Map<Method, MethodDescriptor<Object, Object>> methods = resourceDsc.methods();
		
		// Get requested method descriptors for the resource
		MethodDescriptor<Object, Object> methodDsc = (methods != null) ? methods.get(httpRequest.getMethod()) : null;
		
		// Is requested method supported?
		if ((methodDsc == null)) {

			// Is it resource discovery request? (HTTP OPTIONS)
			if (Method.OPTIONS.equals(httpRequest.getMethod()) && (methods != null)) {
				// Set Allow header - no response content
				setAllowHeader(methods.keySet(), httpResponse);
				httpResponse.setStatus(HttpServletResponse.SC_OK);
				return;
			}
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
		
		ContentAdapter<Object, InputStream> outputContentAdapter = null;

		// Is response body expected?
		if (request.getMethod().isResponseBody()) {
			// Check Accept header
			String acceptHeader = httpRequest.getHeader("Accept");
			
			throw new ErrorException(HttpServletResponse.SC_NOT_ACCEPTABLE);	
		}
		
		if (inputContentAdapter != null) {
			request.setBody(inputContentAdapter.transform(httpRequest.getInputStream()));
		}
		
		// Invoke resource method
		Response<?> response = methodDsc.invoke(request);
		
		if (response == null) {
			throw new ErrorException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		// Write response status
		httpResponse.setStatus((response.getStatus() > 0) ? response.getStatus() : HttpServletResponse.SC_OK);
		
		if (response.getContent() != null) {
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

		if ((HttpServletResponse.SC_CREATED == httpResponse.getStatus())) {
			httpResponse.setHeader(ContentDescriptor.META_LOCATION, response.getContext() + response.getUri());
		} 

		if ((response.getContent().getBody() == null) || (HttpServletResponse.SC_NOT_MODIFIED == httpResponse.getStatus())) {
			return;
		}
		
		// Write response body
		if (outputContentAdapter != null) {
			InputStream is =  outputContentAdapter.transform(response.getContent().getBody());
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
				writeError(e, nativeHttpResponse);
				return;
			}
			throw new ServletException(e);
		}		
	}

	protected static void setAllowHeader(Set<Method> methods, HttpServletResponse response) {
		
		String allow = Method.OPTIONS.name();
		
		if (methods != null) {
			for (Method method : methods) {
				if (!Method.OPTIONS.equals(method)) {
					allow += ", " + method.name();					
				}
			}
		}
		response.setHeader("Allow", allow);
	}
	
	protected void writeError(ErrorException ex, HttpServletResponse httpResponse) {
		httpResponse.setStatus(ex.getStatusCode());
		
		Writer writer = null;
		try {
			httpResponse.setContentType("text/plain");
			
			writer = httpResponse.getWriter();
			ex.printStackTrace(new PrintWriter(writer));
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
